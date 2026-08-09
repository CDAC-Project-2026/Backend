from chromadb import PersistentClient
from langchain_ollama import OllamaEmbeddings

client = PersistentClient("./chroma-db")

collection = client.get_collection(name="effective_java")

embeddings = OllamaEmbeddings(
    model="nomic-embed-text"
)

question = "Why should we prefer composition over inheritance?"

query_vector = embeddings.embed_query(question)

results = collection.query(
    query_embeddings=[query_vector],
    n_results=3
)

print(results["documents"])