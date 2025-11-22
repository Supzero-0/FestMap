export default {
  content: ['./src/**/*.{html,ts}'],

  theme: {
    extend: {
      colors: {
        // 🎨 Palette FestMap
        'fest-bg': '#0A0A0F', // Noir profond
        'fest-surface': '#1E1E28', // Gris anthracite
        'fest-violet-neon': '#3b1fb7',
        'fest-magenta': '#a45bff',
        'fest-orange': '#ff7b36',
        // Utilitaires
        'fest-text': '#F5F5FF', // Texte principal clair
        'fest-border-subtle': 'rgba(255, 255, 255, 0.08)',
      },

      fontFamily: {
        heading: ['"Orbitron"', 'system-ui', 'sans-serif'],
        body: ['"Inter"', 'system-ui', 'sans-serif'],
      },
    },
  },
};
