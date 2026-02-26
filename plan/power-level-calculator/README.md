# Power Level Anvil Cost Calculator

A standalone web application that visualizes the Power Level cost formulas from the Gear Maintenance and Modification mod's anvil economy system.

## How to Run

The calculator is a single HTML file with no build step or server required.

1. **Open in a browser** – Double-click `index.html` or drag it into your browser window.

2. **Or use a local server** (recommended if double-click fails due to CORS):
   ```bash
   cd power-level-calculator
   python3 -m http.server 8000
   ```
   Then open http://localhost:8000 in your browser.

   Alternatively with Node.js:
   ```bash
   npx serve .
   ```

3. **Or open from the command line** (macOS):
   ```bash
   open index.html
   ```

## What It Shows

- **Enchant Cost** – XP cost when adding enchantments from books (sigmoid: `ceil(minCost + (maxCost - minCost) / (1 + e^(-k × (PL - x0))))`; default min 3, max 50, x0 255, k 0.025)
- **Repair Effectiveness** – Durability restored per repair vs. enchantment raw (sigmoid curve; cost is flat: 1 material + 1 level)
- **Combine Cost** – XP cost when combining two items (e.g., two swords); uses the same sigmoid as Enchant Cost based on `combinedRaw = rawLeft + rawRight`. Durability restored uses the Branch A repair formula based on the left item's enchantment PL.

Power level equals raw value (linear identity).
