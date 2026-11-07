# 🌿 HealthKu - Asclepios Edition

Aplikasi Android Native (Kotlin + Jetpack Compose) dengan desain UI/UX **Asclepios Theme (Warm Cream & Sage Green)** yang tersambung **100% langsung ke Android Health Connect** tanpa data dummy/tiruan.

---

### 🎨 Tampilan Layar (Sesuai Referensi Asclepios)
1. **Layar 1 (Dashboard / Health Score):**
   - Header Profil & Notifikasi Status Health Connect.
   - Pill Search bar (`Search aklepios...`).
   - **Asclepios Score Card:** Menampilkan skor kesiapan fisik (Readiness Score 1–100) dengan selector pill hijau sage.
   - **Smart Health Metrics Grid:**
     - Kartu **Heart Rate** dengan ilustrasi organ 3D jantung beranimasi glow dan grafik gelombang.
     - Kartu **Blood Pressure** dengan ilustrasi stetoskop dan indikator denyut.
     - Kartu **Calories Burned** & **Daily Walking (Langkah & Jarak)**.
   - **AI Athletic Coach:** Briefing pagi dan evaluasi malam berbasis data asli tubuh.
2. **Layar 2 (Live Monitor / Blood Pressure):**
   - Pembacaan besar `SYSmm/hg` (121/80).
   - Indikator circular timer/gauge (0s, 15s, 20s) dengan organ 3D jantung di tengah.
   - Overview kartu Saturasi Oksigen (SpO2) dan denyut nadi.
   - Kapsul bawah: *Swipe to start reading*.
3. **Layar 3 (Sleep Overview & Disease Risks):**
   - Kalender mingguan interaktif.
   - Kartu hijau *Sleep Overview* (durasi tidur & fase deep sleep).
   - Riwayat tidur harian dengan status pill (*Optimal*, *Low*, *Rem*, *Insomniac*).
4. **Layar 4 (Settings):**
   - Pengaturan izin akses Health Connect resmi.
   - Konfigurasi opsional Google Gemini API Key.

---

### 🔒 Integrasi Data Real
- Membaca data langsung dari API resmi **Android Health Connect (`androidx.health.connect.client`)**.
- Jika data belum tercatat di HP, sistem menampilkan status apa adanya (`--` / `0`), tanpa data dummy palsu.
