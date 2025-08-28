import { resolve } from 'path';
import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

const pathResolve = (dir) => {
  return resolve(__dirname, '.', dir);
};

export default defineConfig({
  transpileDependencies: ['@dcloudio/uni-ui'],
  plugins: [
    uni(),
  ],
  root: process.cwd(),
  resolve: {
    alias: [
      // 绝对路径重命名：/@/xxxx => src/xxxx
      {
        find: /\/@\//,
        replacement: pathResolve('src') + '/',
      },
      {
        find: /^~/,
        replacement: '',
      },
    ],
  },
  // 添加针对微信小程序的配置
  define: {
    __VUE_OPTIONS_API__: true,
    __VUE_PROD_DEVTOOLS__: false,
  },
  // 优化微信小程序构建
  optimizeDeps: {
    exclude: ['@dcloudio/uni-app']
  },
  // 添加Babel配置解决statuses模块问题
  esbuild: {
    target: 'es2015',
    supported: {
      'top-level-await': true
    }
  },
  // 合并后的build配置
  build: {
    target: 'es2015',
    minify: false,
    rollupOptions: {
      external: ['../lib/statuses'],
      output: {
        globals: {
          '../lib/statuses': 'statuses'
        }
      }
    }
  },
  // 添加针对微信小程序的特殊配置
  css: {
    preprocessorOptions: {
      scss: {
        // 移除全局导入以避免循环导入问题
        // additionalData: '@import "@/theme/index.scss";'
      }
    }
  }
})
