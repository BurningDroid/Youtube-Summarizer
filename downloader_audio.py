import yt_dlp

def download(url):
    ydl_opts = {
        "outtmpl": f"temp/temp.%(ext)s",
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

    return "temp/temp.wav"