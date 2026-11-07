# HealthKu - Unified AI Engineering & Design Guardrails

Proyek ini dipandu oleh tiga pilar standar kualitas:
1. **Anti-Slop (`miqdadbadjuber/anti-slop`)**: Filter penolak template klise, gradien pasaran, dan komentar kode mubazir.
2. **Impeccable (`pbakaus/impeccable`)**: Art Direction, audit hierarki visual, pemolesan micro-spacing, dan kepatuhan mutlak pada `DESIGN.md`.
3. **UI/UX Pro Max (`nextlevelbuilder/ui-ux-pro-max-skill`)**: Ensiklopedia palet harmonis (aturan 60-30-10), ergonomi sentuhan mobile (minimal 44x44dp), dan visualisasi data yang manusiawi.

---

### Aturan Kerja Aktif:
1. **Desain UI:**
   - Wajib mematuhi token dan aturan di `DESIGN.md` (Asclepios Theme).
   - Radius sudut: 26dp untuk Hero Card, 20-22dp untuk Metric Card, 30dp untuk Action Capsule.
   - Kontras warna teks wajib memenuhi standar WCAG AA.
2. **Kualitas Data & Kode:**
   - Wajib menggunakan data asli langsung dari API resmi **Android Health Connect**.
   - Dilarang keras menyisipkan data tiruan/dummy di alur produksi.
   - Jika data belum tersedia dari sensor, tampilkan status apa adanya (`--`), bukan angka halusinasi.
3. **Dokumentasi Kode:**
   - Komentar hanya diperbolehkan jika menjelaskan alasan teknis (*why*), dilarang mengulang sintaks (*what*).
