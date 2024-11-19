import { defineConfig } from 'vite';
import svgr from '@svgr/rollup';
import react from '@vitejs/plugin-react';
import { nodePolyfills } from 'vite-plugin-node-polyfills'


export default defineConfig({
  plugins: [react(), svgr(), nodePolyfills(),],
  base: './',

  build: {
    outDir: 'dist-react',
  },
  assetsInclude: ['**/*.ttf', '**/*.woff', '**/*.woff2'],
  server: {
    port: 5123,
    strictPort: true,
    proxy: {
      '/bf':{
        target: 'http://k11a505.p.ssafy.io',
        changeOrigin: true,
        secure: false,
      },
    }
    },
  },
);
