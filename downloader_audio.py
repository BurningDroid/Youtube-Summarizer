import yt_dlp
import os
import tempfile

def download(url):
    temp_dir = tempfile.gettempdir()
    output_path = os.path.join(temp_dir, "temp.%(ext)s")
    print("output_path: " + output_path)

    ydl_opts = {
        "outtmpl": output_path,  # 임시 폴더에 저장
        "format": "bestaudio/best",
        'audioformat': 'wav',
        "postprocessors": [
            {
                "key": "FFmpegExtractAudio",
                "preferredcodec": "wav",
                "preferredquality": "192",
            },
        ],
    }

    with yt_dlp.YoutubeDL(ydl_opts) as ydl:
        ydl.download(url)

    return os.path.join(temp_dir, "temp.wav")