import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    proxy: {
      '/prices': 'http://localhost:8080',
    },
  },
  build: {
    outDir: '../src/main/resources/static',
  },
  plugins: [react()],
})
