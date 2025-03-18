import os
import whisper

def extract(audio_file):
    print("\n\nSTT 추출 시작...")
    model_size="small"

    model = whisper.load_model(model_size)
    result = model.transcribe(audio_file, fp16=False, word_timestamps=True, language="ko")

    transcribed_texts = []
    for segment in result["segments"]:
        transcribed_texts.append(f"[{segment['start']:.2f} - {segment['end']:.2f}] {segment['text']}")

    stt = "\n".join(transcribed_texts)
    print(stt)
    os.makedirs("temp", exist_ok=True)
    with open("temp/stt.txt", "w", encoding="utf-8") as file:
        file.write(stt)
    
    return stt