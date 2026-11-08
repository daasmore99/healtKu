from PIL import Image, ImageDraw, ImageFilter
from collections import deque

SRC = r"C:\Users\SoftNet\.gemini\antigravity\brain\723c63f1-269f-47f1-95d7-0e12a4d87e0c\.user_uploaded\media_1794074283820_8a2f21a0.png"
OUT = r"app\src\main\res\drawable-nodpi\\"
im = Image.open(SRC).convert("RGBA")


def key_out(img, tol=38):
    # Flood-fill from the border so only the backdrop connected to the edges becomes transparent;
    # dark/saturated subject pixels that merely resemble the backdrop stay opaque.
    px = img.load()
    w, h = img.size
    seen = [[False] * h for _ in range(w)]
    q = deque()
    for x in range(w):
        q.append((x, 0)); q.append((x, h - 1))
    for y in range(h):
        q.append((0, y)); q.append((w - 1, y))
    while q:
        x, y = q.popleft()
        if seen[x][y]:
            continue
        seen[x][y] = True
        r, g, b, a = px[x, y]
        if (r + g + b) / 3 < 150 or max(r, g, b) - min(r, g, b) > 70:
            continue
        nbrs = [(x + dx, y + dy) for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1))]
        ref = None
        for nx, ny in nbrs:
            if 0 <= nx < w and 0 <= ny < h and not seen[nx][ny]:
                nr, ng, nb, _ = px[nx, ny]
                if abs(nr - r) + abs(ng - g) + abs(nb - b) < tol:
                    q.append((nx, ny))
        px[x, y] = (r, g, b, 0)
    alpha = img.split()[3].filter(ImageFilter.GaussianBlur(0.6))
    img.putalpha(alpha)
    return img


def save(box, name, scale=4, keyed=True):
    c = im.crop(box)
    c = c.resize((c.width * scale, c.height * scale), Image.Resampling.LANCZOS)
    c = c.filter(ImageFilter.UnsharpMask(radius=1.5, percent=90, threshold=2))
    if keyed:
        c = key_out(c)
    c.save(OUT + name)
    print(name, c.size)


save((474, 298, 540, 398), "heart_3d.png")
save((124, 474, 182, 556), "heart_card_mini.png")
save((246, 474, 328, 556), "stethoscope_heart.png")
save((713, 392, 800, 475), "sleep_character.png", keyed=False)
save((80, 134, 123, 178), "avatar_user.png", keyed=False)
