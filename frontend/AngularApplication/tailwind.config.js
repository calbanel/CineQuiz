/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {},
    backgroundImage: {
      'cinema': "url('/assets/scene.png')",
    },
    colors: {
      'cine-red': '#ad3133',
      'jalapeno-red': '#b71540',
      'orange': '#e58e26',
      'good-samaritan': '#3c6382',
      'forrest-blues': '#0a3d62',
      'waterfall': '#38ada9',
      'aurora-green': '#78e08f',
      'white': '#FFFFFF',
      'reef-encounter': '#079992',
      'spray':'#82ccdd',
    },
    fontFamily: {
      roboto: ['./src/assets/roboto/Roboto-Black.ttf'],
    },
  },
  plugins: [],
}
