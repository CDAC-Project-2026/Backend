from pathlib import Path

from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain.embeddings import init_embeddings
from chromadb import PersistentClient


EMB_MODEL = "ollama:nomic-embed-text"
DB_PATH = "./chroma-db"
COLLECTION_NAME = "exam_portal_knowledge"

emb_model = init_embeddings(EMB_MODEL)

client = PersistentClient(DB_PATH)
collection = client.get_or_create_collection(COLLECTION_NAME)

splitter = RecursiveCharacterTextSplitter(
    chunk_size=1000,
    chunk_overlap=150
)


subjects = {
    "java": "./pdfs/java",
    "cpp": "./pdfs/cpp",
    "sql": "./pdfs/sql",
    "react": "./pdfs/react"
}


ids = []
documents = []
metadatas = []


for subject, folder in subjects.items():

    folder = Path(folder)

    if not folder.exists():
        print(f"{subject}: folder not found")
        continue

    for pdf in folder.glob("*.pdf"):

        print(f"\nReading {pdf.name} ({subject})")

        pages = PyPDFLoader(str(pdf)).load()
        chunks = splitter.split_documents(pages)

        print(f"Pages: {len(pages)}")
        print(f"Chunks: {len(chunks)}")

        for i, chunk in enumerate(chunks):

            ids.append(f"{subject}_{pdf.stem}_{i}")
            documents.append(chunk.page_content)

            metadatas.append({
                "subject": subject,
                "source": pdf.name,
                "page": chunk.metadata.get("page"),
                "page_label": chunk.metadata.get("page_label", "")
            })


print("\nTotal chunks:", len(documents))
print("Creating embeddings...")

embeddings = emb_model.embed_documents(documents)

print("Saving to ChromaDB...")

batch_size = 5000

for i in range(0, len(ids), batch_size):

    collection.upsert(
        ids=ids[i:i + batch_size],
        documents=documents[i:i + batch_size],
        embeddings=embeddings[i:i + batch_size],
        metadatas=metadatas[i:i + batch_size]
    )

    print(f"Saved {min(i + batch_size, len(ids))}/{len(ids)}")

print("Done.")
print("Collection:", COLLECTION_NAME)
print("Documents:", len(documents))