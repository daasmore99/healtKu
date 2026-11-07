# Project Standards & Anti-Slop Policy

Semua kode, desain UI, dan teks dalam proyek ini wajib mematuhi standar **Anti-Slop** (`miqdadbadjuber/anti-slop`):

1. **Desain UI:**
   - Gunakan palet warna yang terkurasi (*Asclepios warm organic / sage green*). Hindari gradien ungu/indigo klise khas AI.
   - Jangan gunakan komentar kode yang hanya mengulang sintaks (misal: `// return result`). Tulis komentar hanya jika menjelaskan *alasan teknis* (*why*), bukan *apa yang dilakukan kode* (*what*).
   - Pastikan hierarki visual rapi, kontras teks terbaca jelas (WCAG AA), dan ukuran tombol sentuh minimal 44x44dp.

2. **Kualitas Data:**
   - Gunakan data asli dari API resmi (**Android Health Connect**), tidak boleh menyisipkan data dummy/tiruan palsu di alur produksi.
   - Tangani kondisi ketika data kosong atau izin belum aktif dengan tampilan yang elegan (`--`), bukan angka halusinasi.
