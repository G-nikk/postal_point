import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url' // Более современный способ

export default defineConfig({
  plugins: [vue()],
  resolve: {
    extensions: ['.ts', '.vue', '.js'],
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
});
