# ToDoToDoList-Jadwal

ToDoList-Jadwal adalah aplikasi manajemen produktivitas yang dirancang khusus untuk mahasiswa atau pelajar guna mengelola beban akademik secara terorganisir. Aplikasi ini menggabungkan dua fitur utama: manajemen tugas/ujian dan pengaturan jadwal kuliah harian dalam satu platform yang sederhana dan intuitif.
Dengan menggunakan arsitektur Android modern (MVVM, Room Database, dan Navigation Component), aplikasi ini memastikan data tersimpan secara lokal dengan aman dan antarmuka yang responsif.

---
[![GitHub latest commit](https://badgen.net/github/last-commit/SculptorXZ/ToDo)](https://GitHub.com/Naereen/StrapDown.js/commit/)
## Fitur Utama:

1. **Manajemen Tugas & Ujian**: Mencatat tenggat waktu (deadline) tugas atau jadwal ujian agar tidak ada yang terlewat.
2. **Jadwal Kuliah**: Menyusun jadwal mata kuliah mingguan berdasarkan hari, jam, dan lokasi ruangan.
3. **Navigasi Cepat**: Perpindahan antar menu yang mudah menggunakan Bottom Navigation Bar.
4. **Penyimpanan Offline**: Data tetap tersimpan di perangkat meskipun tidak ada koneksi internet menggunakan Room Database.


## Cara Penggunaan Aplikasi

1. Navigasi Utama
Di bagian bawah layar, terdapat dua menu utama:
• Tugas dan Ujian: Klik ikon "Todo" untuk melihat atau menambah daftar tugas.
• Jadwal Kuliah: Klik ikon "Jadwal" untuk melihat susunan jadwal kuliah Anda.

2. Mengelola Jadwal Kuliah
• Melihat Jadwal: Masuk ke menu "Jadwal Kuliah". Daftar jadwal akan ditampilkan urut berdasarkan waktu atau hari.
• Menambah Jadwal Baru:
  - i. Klik tombol tambah (+) atau Floating Action Button di pojok kanan bawah.
  - ii. Isi data yang diperlukan: Nama Mata Kuliah, Nama Dosen, Hari, Jam Mulai, Jam Selesai, dan Ruangan.
  - iii. Klik Simpan.
• Menghapus Jadwal: Tekan lama pada salah satu item jadwal atau klik ikon hapus (jika tersedia) untuk membersihkan jadwal yang sudah tidak relevan.

3. Mengelola Tugas dan Ujian
• Melihat Daftar Tugas: Masuk ke menu "Tugas dan Ujian". Anda akan melihat daftar pekerjaan rumah atau rencana ujian.
•Menambah Tugas:
  - i.Klik tombol tambah (+).
  - ii.Masukkan judul tugas, deskripsi singkat, dan tanggal tenggat waktu.
  - iii.Klik Simpan.
• Menandai Selesai: Anda bisa menghapus atau menandai tugas yang sudah dikerjakan agar daftar tetap rapi

4. Pengaturan Waktu
Saat menambah jadwal atau tugas, aplikasi akan memunculkan Time Picker (pemilih jam) atau Date Picker (pemilih tanggal). Cukup pilih waktu yang sesuai, lalu tekan OK agar format waktu tercatat dengan benar secara otomatis.
