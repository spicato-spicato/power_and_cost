# Release changelog template

Copy this file to `releases/{minecraft_version}-{mod_version}.md` before creating a GitHub Release.

Write for players, not developers. List **only what changed since the previous released version** — not what the mod already does.

---

## Example (feature release)

```markdown
- Anvil repair costs now scale with enchantment power level instead of vanilla XP cost
- Renaming enchanted gear on an anvil costs less when the item has fewer enchantments
```

## Example (Minecraft version port, no behavior change)

```markdown
- Minecraft 1.21.11 support
```

## Example (first Modrinth release)

```markdown
- Initial release for Minecraft 1.21.11
```

## Guidelines

- Use plain language ("anvil" not "AnvilMenuMixin")
- One bullet per player-visible change **new in this release**
- Do not restate existing mod features from README or player guides
- Omit internal refactors, test additions, and CI changes unless players notice them
- Do not copy PR titles or commit messages
