# Power and Cost

A Minecraft Fabric mod for enchanted gear maintenance and modification cost-power relationships.

## Compatibility

- **Minecraft:** 1.21.10
- **Fabric Loader:** ≥ 0.18.4
- **Fabric API:** Required
- **Java:** ≥ 21

## Build Instructions

From the mod project directory:

```bash
./gradlew build
```

The built JAR will be in `build/libs/`.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.10
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Copy the built `power_and_cost-*.jar` from `build/libs/` into your Minecraft `mods` folder

## Branch Policy

Each supported Minecraft version has its own long-lived branch named after the exact `minecraft_version` in `gradle.properties` (e.g. `1.21.10`). Version branches are the source of truth; `main` is not used.

- **Default branch:** latest supported version (`1.21.10`)
- **Feature/fix work:** branch from the target version as `{version}/feature-name`, merge back into the version branch
- **New MC version:** create `{version}` from the prior version branch, bump `gradle.properties`, push to origin
- **Retiring a version:** tag the final release, then archive the branch (do not delete)

```bash
git clone https://github.com/spicato-spicato/gear_maintinence_and_modification.git
cd gear_maintinence_and_modification
git checkout 1.21.10
```
