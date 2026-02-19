# Gear Maintenance and Modification — Project Plan

## Project Structure (Tree)

```
                    ┌─────────────────────────────────────┐
                    │   CORE: Item Power Level Mechanic    │
                    │   (Foundation for all anvil costs)   │
                    └─────────────────┬───────────────────┘
                                      │
                    ┌─────────────────┴─────────────────┐
                    │                                   │
                    ▼                                   ▼
    ┌───────────────────────────────┐   ┌───────────────────────────────┐
    │  BRANCH A: PL → Durability     │   │  BRANCH B: Enchantment Cost    │
    │  Repair (linear inverse)       │   │  Curve-based cost (raw^0.75)   │
    └───────────────────────────────┘   └───────────────────────────────┘
```

---

## Core: Item Power Level Mechanic

The item power level (PL) is the central metric that drives the anvil economy. It is derived from a **raw value** (linear sum) converted through a **gradual exponential curve** to produce the final PL used for costs.

### Raw Value (Linear Inputs)

- **Base item tier** (e.g., Wood=10, Stone=20, Gold=30, Iron=40, Diamond=70, Netherite=100)
- **Enchantments** (each enchantment contributes a value to the raw sum; see Branch B examples for the full scheme)

### Power Level Conversion (Gradual Exponential Curve)

Raw values are converted to power level via a sub-linear power curve so that costs scale more gently:

```
powerLevel = round(rawValue^0.75)
```

This curve compresses high raw values: doubling raw does not double PL. Example: raw 100 → PL 32; raw 200 → PL 53. The exponent 0.75 produces a gradual curve that lowers enchantment and combine costs for high-tier gear.

| rawValue | powerLevel |
|----------|------------|
| 10 | 6 |
| 50 | 19 |
| 100 | 32 |
| 150 | 42 |
| 200 | 53 |

All downstream cost systems (repair, enchant, combine) use this power level.

---

## Branch A: Power Level → Durability Repair Relationship

### Mechanic

Durability restored per repair is determined by **enchantment power level only** (material/base tier is excluded). The relationship is **linear inverse**: higher enchantment power level → less durability restored, at a constant rate. Cost is always 1 material + 1 level.

| Aspect | Behavior |
|--------|----------|
| **Durability per repair** | `max(0, 1 - enchantmentPL / ceiling)` × vanilla repair quantity — 1 = vanilla amount |
| **Cost per repair** | Fixed at **1 material item** and **1 XP level** (material PL not used) |

### Power Level → Durability Restored

| Variable | Definition |
|----------|------------|
| `enchantmentPL` | `round(enchantmentRaw^0.75)` — enchantment raw only, excludes base tier |
| `ceiling` | PL at which durability reaches 0; larger values = more gradual decline (e.g., 75) |
| `durabilityRestored` | Multiplier of vanilla repair quantity (1 = vanilla amount for that action) |

**Relationship formula:**

```
durabilityRestored = max(0, 1 - enchantmentPL / ceiling) × vanillaRepairQuantity
```

where `vanillaRepairQuantity` is the durability that vanilla Minecraft would restore for that repair action. The multiplier is 1 for unenchanted items and decreases linearly with enchantment power level.

- **Pinned at 1**: When `enchantmentPL = 0` (no enchants), durability restored = 1× vanilla repair quantity (maximum).
- **Linear inverse**: Durability decreases by a constant amount per PL. Slope = `-1/ceiling`.
- **Ceiling**: The `ceiling` parameter sets the PL at which durability hits 0. Larger `ceiling` → slower decline (e.g., `ceiling = 75` keeps heavily enchanted items more repairable).
- **Material excluded**: Base item tier does not affect durability repair; material cost is always 1 regardless of tier.

### Examples (Enchantment PL → Durability per Repair)

Using `ceiling = 75`. Cost is always 1 material, 1 level.

**Iron sword + Sharpness I:**

| Item | enchantmentRaw | enchantmentPL | Durability restored (× vanilla) |
|------|----------------|---------------|--------------------------------|
| Iron sword + Sharpness I | 12 | 6 | 0.92 |

**Diamond sword — natural progression:**

| Stage | Item state | enchantmentRaw | enchantmentPL | Durability restored (× vanilla) |
|-------|------------|---------------|---------------|--------------------------------|
| 1 | Diamond sword | 0 | 0 | 1.00 |
| 2 | + Sharpness IV | 48 | 18 | 0.76 |
| 3 | + Sharpness IV → Sharpness V | 60 | 22 | 0.71 |
| 4 | + Looting III | 84 | 28 | 0.63 |
| 5 | + Knockback II | 100 | 32 | 0.57 |
| 6 | + Unbreaking III | 124 | 37 | 0.51 |
| 7 | + Fire Aspect II | 140 | 41 | 0.45 |
| 8 | + Sweeping Edge III | 164 | 46 | 0.39 |

*Implication: With ceiling = 75, durability drops linearly from 1× vanilla (no enchants) to 0.39× vanilla (full sword). Items with enchantmentPL ≥ 75 restore 0 durability per repair.*

---

## Branch B: Enchantment Cost

### Mechanic

- **Formula**: `combinedRaw = existingRaw + newEnchantRaw` → `combinedPL = round(combinedRaw^0.75)` → `costInLevels = ceil(combinedPL / 10)`
- Cost uses the power-level curve: combined raw value is converted via the gradual exponential, then divided by 10.
- Higher-tier items and stronger enchantments increase cost, but the curve keeps costs lower than linear scaling.

### Power Level → XP Cost Conversion

| Variable | Definition |
|----------|------------|
| `combinedRaw` | `existingRaw + newEnchantRaw` (item raw value before enchant + raw value of the enchantment being added) |
| `combinedPL` | `round(combinedRaw^0.75)` (power level from the gradual exponential curve) |
| `costInLevels` | XP levels charged for the enchantment action |

**Conversion formula:**

```
costInLevels = ceil(round(combinedRaw^0.75) / 10)
```

### Examples (Enchantment Cost)

**Enchant value scheme:** Sharpness 12/level (I=12, V=60); Unbreaking 8/level (III=24); Knockback 8/level (II=16); Fire Aspect 8/level (II=16); Looting 8/level (III=24); Sweeping Edge 8/level (III=24).

**Iron sword + Sharpness I:**

| Item (existingRaw) | Adding book (raw) | combinedRaw | combinedPL | Cost |
|--------------------|-------------------|-------------|------------|------|
| Iron sword (40) | Sharpness I (12) | 52 | 19 | 2 levels |

**Diamond sword — natural progression:**

| Stage | Item state | Adding book | combinedRaw | combinedPL | Cost |
|-------|------------|-------------|-------------|------------|------|
| 1 | Diamond sword (70) | Sharpness IV (48) | 118 | 36 | 4 levels |
| 2 | + Sharpness IV (118) | Sharpness V (60) | 178 | 49 | 5 levels |
| 3 | + Sharpness V (130) | Looting III (24) | 154 | 44 | 5 levels |
| 4 | + Looting III (154) | Knockback II (16) | 170 | 47 | 5 levels |
| 5 | + Knockback II (170) | Unbreaking III (24) | 194 | 53 | 6 levels |
| 6 | + Unbreaking III (194) | Fire Aspect II (16) | 210 | 56 | 6 levels |
| 7 | + Fire Aspect II (210) | Sweeping Edge III (24) | 234 | 60 | 6 levels |

*Final sword: Diamond + Sharpness V, Looting III, Knockback II, Unbreaking III, Fire Aspect II, Sweeping Edge III (raw 234). Total cost: 37 levels.*

### Details

- When adding an enchantment from a book, the cost depends on the item’s current raw value plus the raw value of the new enchantment. The combined raw is converted to power level via the gradual exponential curve.
- This branch is independent of the repair refactor but shares the same power level foundation.

---

## Branch C (if applicable): Combine Cost

- **Formula**: `totalRaw = rawA + rawB` → `combinedPL = round(totalRaw^0.75)` → `costInLevels = ceil(combinedPL / 10)`
- Cost when combining two items (e.g., two swords for durability). Uses the same power-level curve.
- Symmetric: combining items with the same total raw value has the same cost regardless of how it is split.

### Examples (Combine Cost)

| Item A | Item B | totalRaw | combinedPL | Cost |
|--------|--------|----------|------------|------|
| 2× Wood (10 each) | — | 20 | 10 | 1 level |
| 2× Iron (40 each) | — | 80 | 27 | 3 levels |
| 2× Diamond (70 each) | — | 140 | 38 | 4 levels |
| Netherite (100) | Diamond (70) | 170 | 42 | 5 levels |
| 2× Netherite (100 each) | — | 200 | 53 | 6 levels |
| Diamond + Sharpness I (82) | Diamond (70) | 152 | 43 | 5 levels |

---

## Implementation Order

1. **Core**: Implement and stabilize the item power level calculation.
2. **Branch A**: Implement power level → durability repair relationship (inverse; cost fixed at 1 item + 1 level).
3. **Branch B**: Implement or refine the linear enchantment cost formula.
4. **Branch C**: Implement or refine the combine cost formula (if part of scope).
