from PIL import Image

src_path = r"C:\Users\SoftNet\.gemini\antigravity\brain\723c63f1-269f-47f1-95d7-0e12a4d87e0c\.user_uploaded\media_1794074283820_8a2f21a0.png"
im = Image.open(src_path)

# 1. 3D Anatomical Heart from Screen 2 center dial
# Center of screen 2 dial is around x: 460-530, y: 395-500
heart_box = (460, 395, 532, 505)
heart_crop = im.crop(heart_box)
heart_crop.save(r"app\src\main\res\drawable-nodpi\heart_3d.png")

# 2. Stethoscope + Heart from Screen 1 Blood Pressure card
# Box in screen 1: x: 260-322, y: 615-725
steth_box = (260, 618, 324, 725)
steth_crop = im.crop(steth_box)
steth_crop.save(r"app\src\main\res\drawable-nodpi\stethoscope_heart.png")

# 3. Heart Rate Card Heart from Screen 1
heart_card_box = (120, 618, 175, 722)
heart_card_crop = im.crop(heart_card_box)
heart_card_crop.save(r"app\src\main\res\drawable-nodpi\heart_card_mini.png")

# 4. Serene Sleep Character from Screen 3 Sleep Overview
sleep_char_box = (695, 518, 782, 622)
sleep_crop = im.crop(sleep_char_box)
sleep_crop.save(r"app\src\main\res\drawable-nodpi\sleep_character.png")

# 5. Profile Avatar (Michelle)
avatar_box = (76, 172, 122, 234)
avatar_crop = im.crop(avatar_box)
avatar_crop.save(r"app\src\main\res\drawable-nodpi\avatar_user.png")

print("All real assets successfully extracted from source design!")
