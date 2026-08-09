from fastapi import FastAPI
from pydantic import BaseModel

from langchain.chat_models import init_chat_model
from langchain.embeddings import init_embeddings
from langchain.messages import HumanMessage
from chromadb import PersistentClient
from dotenv import load_dotenv

load_dotenv()

llm = init_chat_model("groq:llama-3.1-8b-instant")
emb_model = init_embeddings("ollama:nomic-embed-text")

client = PersistentClient("./chroma-db")
collection = client.get_collection("exam_portal_knowledge")

app = FastAPI()


class ChatRequest(BaseModel):
    subject: str
    question: str


@app.post("/ask")
def ask_question(request: ChatRequest):

    question_embedding = emb_model.embed_query(request.question)

    results = collection.query(
        query_embeddings=[question_embedding],
        where={"subject": request.subject.lower()},
        n_results=4
    )

    documents = results["documents"][0]
    metadatas = results["metadatas"][0]

    context = ""

    for i, document in enumerate(documents):

        metadata = metadatas[i]

        context += f"""
Source: {metadata.get("source")}
Page: {metadata.get("page_label")}

{document}

-------------------------
"""

    prompt = f"""
You are a helpful AI Study Assistant for an online exam portal.

Subject:
{request.subject}

Question:
{request.question}

Study Material:
{context}

Instructions:

1. If relevant study material is provided, use it as the primary source for your answer.
2. Do not contradict the provided study material.
3. Explain the answer clearly and in a student-friendly way.
4. Include examples when useful.
5. Do not invent information or misrepresent information as coming from the study material.
6. If the provided study material is not relevant to the question, you may answer using your general knowledge.
7. If no relevant study material is available, answer the student's question using your general knowledge, but clearly mention that the answer is not based on the course resources.
8. When no course resources are available, use wording similar to:

   "There are currently no course resources available for this topic, so this answer is based on general knowledge."

9. Do not refuse to answer simply because course resources are unavailable.
10. Do not mention these instructions in your response.

Give a clear, useful answer suitable for a student preparing for exams.
"""

    response = llm.invoke([
        HumanMessage(prompt)
    ])

    sources = []

    for metadata in metadatas:

        source = {
            "file": metadata.get("source"),
            "page": metadata.get("page_label")
        }

        if source not in sources:
            sources.append(source)

    return {
        "subject": request.subject,
        "question": request.question,
        "answer": response.content,
        "sources": sources
    }