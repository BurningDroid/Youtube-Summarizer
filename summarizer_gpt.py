from openai import OpenAI

client = OpenAI()

def split_text(text, max_chunk_size=10000):
    print(" - 부분 텍스트로 분해...")
    chunks = []
    while len(text) > max_chunk_size:
        split_index = text.rfind(".", 0, max_chunk_size)
        if split_index == -1:
            split_index = max_chunk_size
        chunks.append(text[:split_index+1])
        text = text[split_index+1:]
    chunks.append(text)
    return chunks

def summarize_text(text):
    print(" - 요약 정리...")
    prompt = f"다음은 영상에서 추출한 내용입니다. 핵심 내용 위주로 정리해주세요. 보기 좋은 포맷으로 작성해주세요\n\n{text}"
    
    completion = client.chat.completions.create(
        model="gpt-4o",
        messages=[{"role": "user", "content": prompt}]
    )

    return completion.choices[0].message.content

def summarize_chunk_text(text):
    print(" - 부분 요약 정리...")
    prompt = f"다음은 영상에서 추출한 내용 중 일부분입니다. 핵심 내용 위주로 정리해주세요.\n\n{text}"
    
    completion = client.chat.completions.create(
        model="gpt-4o",
        messages=[{"role": "user", "content": prompt}]
    )

    return completion.choices[0].message.content

def summarize_chunk_results(text):
    print(" - 요약 정리 조각들을 하나로 통합...")
    prompt = f"다음은 하나의 영상에서 추출한 내용을 여러 토큰으로 나눈 뒤 각각을 요약 정리한 내용입니다. 이 내용들을 다시 하나로 합쳐서 정리해주세요. 만약 가능하다면 주제, 소주제 등으로 구분해줘도 좋습니다.\n\n{text}"
    
    completion = client.chat.completions.create(
        model="gpt-4o",
        messages=[{"role": "user", "content": prompt}]
    )

    return completion.choices[0].message.content

def summarize(text, model="gpt-4o"):
    if len(text) <= 10000:
        return summarize_text(text)
    
    text_chunks = split_text(text)
    summarized_chunks = [summarize_chunk_text(chunk) for chunk in text_chunks]
    final_summary = summarize_chunk_results("\n".join(summarized_chunks))
    return final_summary
    
