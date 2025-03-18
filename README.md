# Youtube-Summarizer
유튜브 영상을 텍스트로 요약하는 파이선 프로그램입니다.
음원만 추출 -> STT로 텍스트 추출 -> GPT로 요약 정리합니다.


## 필요한 의존성 설치
 - yt-dlp
 - ffmpeg
 - whisper
 - openai


### yt-dlp
Youtube 영상에서 음원 다운로드를 위해 yt-dlp를 사용합니다.
```
pip install yt-dlp
```


### ffmpeg
yt-dlp를 통해 다운받은 영상에서 음원만 추출하기 위해 ffmpeg이 필요합니다.
```
brew install ffmpeg
```


### whisper
음원에서 텍스트를 추출하기 위해(STT) OpenAI의 whisper를 사용합니다.
whisper는 오픈 소스로 로컬에서 구동되며 별도 비용이 발생하진 않습니다.
(주의: `whisper`가 아니라 `openai-whisper`를 설치해야 합니다.)
```
pip install openai-whisper
```

### openai
텍스트를 다시 요약 정리하기 위해 OpenAI의 Chat Completions API를 사용합니다.
모델은 gpt-4o이며 해당 API는 키가 필요합니다.
```
pip install openai
```

다음과 같이 OpenAI Key를 환경변수로 등록해주세요



# 실행
```
python main.py "YOUTUBE_URL"
```
