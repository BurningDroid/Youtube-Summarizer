from downloader_audio import download
from extractor_whisper import extract
from summarizer_gpt import summarize
import time
import argparse


start_time = time.time()

parser = argparse.ArgumentParser(description="Youtube 영상을 요약 정리합니다.")
parser.add_argument("url", type=str, help="Youtube 영상의 URL을 입력하세요.")
args = parser.parse_args()
url = args.url

audio_file = download(url)

extract_stime = time.time()
raw_text = extract(audio_file)
print(raw_text)
print(f"\nSTT 추출 수행 시간: {time.time()-extract_stime}초")

print("\n\n핵심 내용 요약 정리 시작...")
summarize_stime = time.time()
answer = summarize(raw_text)
print(f"\n요약 정리 수행 시간: {time.time()-summarize_stime}초")
print(f"\n<RESULT>\n{answer}")


print(f"\n\n⏳ 전체 수행 시간: {time.time()-start_time:.4f}초")