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
    │  Repair (sigmoid)             │   │  Sigmoid cost (raw)           │
    └───────────────────────────────┘   └───────────────────────────────┘
```

---

## Core: Item Power Level Mechanic

The item power level (PL) is the central metric that drives the anvil economy. It is derived from a **raw value** (linear sum) via a **linear (1:1) identity**: powerLevel = rawValue.

### Raw Value (Linear Inputs)

- **Base item tier** — tiered tools, weapons, and armor
- **Enchantments** — each enchantment contributes a value to the raw sum (`perLevel × level`)
- **Non-material items** — bows, fishing rods, etc. use fixed base values

Use the power-level calculator sandbox to tune material, enchantment, and non-material raw values.

### Power Level Conversion (Linear Identity)

Raw values equal power level with no conversion:

```
powerLevel = rawValue
```

All downstream cost systems (repair, enchant, combine) use this power level.

---

## Branch A: Power Level → Durability Repair Relationship

### Mechanic

Durability restored per repair is determined by **enchantment power level only** (material/base tier is excluded). The relationship uses a **sigmoid curve**: slow decrease at low power, steep drop in the middle, asymptote toward a floor as power approaches the theoretical max. Cost is always 1 material + 1 level.

| Aspect | Behavior |
|--------|----------|
| **Durability per repair** | `(minRepair + (maxRepair - minRepair) / (1 + e^(k × (enchantmentPL - x0))))` × vanilla repair quantity |
| **Cost per repair** | Fixed at **1 material item** and **1 XP level** (material PL not used) |

### Power Level → Durability Restored

| Variable | Definition |
|----------|------------|
| `enchantmentPL` | `enchantmentRaw` (identity) — enchantment raw only, excludes base tier |
| `minRepair` | Floor multiplier (e.g., 0.25) — asymptote as power approaches max (may dip slightly below 0.33) |
| `maxRepair` | Ceiling multiplier (e.g., 1.1) — unenchanted items approach 1× vanilla |
| `x0` | Midpoint (e.g., 100) — power level at steepest part of the curve |
| `k` | Steepness (e.g., 0.025) — controls how sharp the transition is |
| `durabilityRestored` | Multiplier of vanilla repair quantity (1 = vanilla amount for that action) |

**Relationship formula:**

```
durabilityRestored = (minRepair + (maxRepair - minRepair) / (1 + e^(k × (enchantmentPL - x0)))) × vanillaRepairQuantity
```

where `vanillaRepairQuantity` is the durability that vanilla Minecraft would restore for that repair action.

- **Sigmoid shape**: Low-power items repair near `maxRepair` (e.g., 1× vanilla) with slow decline. As power increases through the midpoint, the factor drops steeply. At high power (approaching theoretical max), the curve tapers toward `minRepair` (e.g., 0.25× vanilla).
- **No hard floor at 0**: Heavily enchanted items still restore some durability, making endgame gear repairable but costly in materials.
- **Material excluded**: Base item tier does not affect durability repair; material cost is always 1 regardless of tier.

---

## Branch B: Enchantment Cost

### Mechanic

- **Formula**: `combinedRaw = existingRaw + newEnchantRaw` → `combinedPL = combinedRaw` → `costInLevels = ceil(minCost + (maxCost - minCost) / (1 + e^(-k × (combinedPL - x0))))`
- Cost uses power level (identity): combined raw equals combined PL. A **sigmoid curve** maps power level to XP cost: low power → ~3 levels (floor), high power → ~50 levels (ceiling). As power approaches the theoretical max, cost tapers off at the ceiling.

### Power Level → XP Cost Conversion

| Variable | Definition |
|----------|------------|
| `combinedRaw` | `existingRaw + newEnchantRaw` (item raw value before enchant + raw value of the enchantment being added) |
| `combinedPL` | `combinedRaw` (power level, identity) |
| `minCost` | Floor (e.g., 3) — asymptote at low power |
| `maxCost` | Ceiling (e.g., 50) — asymptote near theoretical max |
| `x0` | Midpoint (e.g., 220) — power level at inflection point (cost ≈ halfway between min and max) |
| `k` | Steepness (e.g., 0.025) — controls how sharp the transition is |
| `costInLevels` | XP levels charged for the enchantment action |

**Conversion formula:**

```
costInLevels = ceil(minCost + (maxCost - minCost) / (1 + e^(-k × (combinedPL - x0))))
```

- **Sigmoid shape**: Low-power items cost near `minCost` (e.g., 3 levels). As power increases through the midpoint, cost rises steeply. At high power (approaching theoretical max), the curve tapers toward `maxCost` (e.g., 50 levels).

### Details

- When adding an enchantment from a book, the cost depends on the item’s current raw value plus the raw value of the new enchantment. Combined raw equals power level (identity).
- The sigmoid ensures early enchants stay cheap and endgame enchants cap at a predictable maximum.
- This branch is independent of the repair refactor but shares the same power level foundation.

---

## Branch C: Item-to-Item Combine

Item-to-item combine is a **dual action**: (1) adding enchantments from the right item to the left, and (2) repairing the left item using durability from the right. Both sub-actions use the same formulas as book-based operations.

### Enchantment Cost (Branch B Formula)

The XP level cost for combining items matches the cost for combining books with books or books with items.

| Variable | Definition |
|----------|------------|
| `rawLeft` | Base tier + enchantments of the left (target) item |
| `rawRight` | Base tier + enchantments of the right (sacrifice) item |
| `combinedRaw` | `rawLeft + rawRight` — the resulting item's total power level after the merge |
| `costInLevels` | Same formula as Branch B (sigmoid) |

**Formula:**

```
combinedRaw = rawLeft + rawRight
combinedPL = combinedRaw
costInLevels = ceil(minCost + (maxCost - minCost) / (1 + e^(-k × (combinedPL - x0))))
```

Uses the same sigmoid parameters as Branch B: `minCost`, `maxCost`, `x0`, `k`.

### Durability Restored (Branch A Formula)

The amount of durability restored follows the same repair penalty logic as Branch A.

| Variable | Definition |
|----------|------------|
| `enchantmentPL` | **Left item's** enchantment raw only (excludes base tier) |
| `vanillaRepairQuantity` | Durability that vanilla Minecraft would restore for this combine action (derived from the right item's remaining durability) |

**Formula:**

```
durabilityRestored = (minRepair + (maxRepair - minRepair) / (1 + e^(k × (enchantmentPL - x0)))) × vanillaRepairQuantity
```

### Example

Left sword: 50% durability, Unbreaking 3. Right sword: 100% durability, Sharpness 5.

- **Enchantment cost**: Based on the combined raw of the resulting item (Unbreaking 3 + Sharpness 5 + base). Uses Branch B sigmoid.
- **Durability restored**: The repair multiplier is calculated using the left item's enchantment PL (Unbreaking 3 raw value). The actual durability transferred from the right item is scaled by this sigmoid factor.

---

## Implementation Order

1. **Core**: Implement and stabilize the item power level calculation.
2. **Branch A**: Implement power level → durability repair relationship (inverse; cost fixed at 1 item + 1 level).
3. **Branch B**: Implement or refine the sigmoid enchantment cost formula.
4. **Branch C**: Implement or refine the combine cost formula (if part of scope).
