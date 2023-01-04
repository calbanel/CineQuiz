/** @type {import('tailwindcss').Config} */

const colors = require('tailwindcss/colors');

module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {},
    backgroundImage: {
      'cinema': "url('/assets/movie-theater.jpg')",
    },
    colors: {
      transparent: 'transparent',
      current: 'currentColor',
      black: colors.black,
      white: colors.white,
      gray: colors.gray,
      emerald: colors.emerald,
      indigo: colors.indigo,
      yellow: colors.yellow,
      red: colors.red,
      blue: colors.blue,
      green: colors.green,
      pink: colors.pink,
      'cine-red': '#ad3133',
      'jalapeno-red': '#b71540',
      'orange': '#e58e26',
      'good-samaritan': '#3c6382',
      'forrest-blues': '#0a3d62',
      'waterfall': '#38ada9',
      'green': '#78e08f',
      'white': '#FFFFFF',
      'reef-encounter': '#079992',
      'spray':'#82ccdd',
    },
    fontFamily: {
      roboto: ['./src/assets/roboto/Roboto-Black.ttf'],
    },
  },
  plugins: [
  ],
}
