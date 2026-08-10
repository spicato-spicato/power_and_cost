# Release changelog template

Copy this file to `releases/{minecraft_version}-{mod_version}.md` before creating a GitHub Release.

Write for players, not developers. Focus on what changed in the game.

---

## Example

```markdown
- Anvil repair costs now scale with enchantment power level instead of vanilla XP cost
- Renaming enchanted gear on an anvil costs less when the item has fewer enchantments
```

## Guidelines

- Use plain language ("anvil" not "AnvilMenuMixin")
- One bullet per player-visible change
- Omit internal refactors, test additions, and CI changes unless players notice them
- Do not copy PR titles or commit messages
