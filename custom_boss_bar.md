# Custom Boss Bar Tutorial

This feature allows you to **replace the health bar textures of any boss in this mod** via resource packs, so that the health bar’s appearance better matches your preferences or the theme of your modpack.

---

## 📁 Resource Pack Structure

Create the following path inside your resource pack:

```
assets/eeeabsmobs/textures/gui/boss_bar/
```

Then place two images there, named after the boss’s registry ID:

- `<boss_id>_base.png`   – Base bar (optional)
- `<boss_id>_overlay.png` – Decorative overlay (optional)

> `<boss_id>` is the entity registry ID (e.g. `relic_annihilator`, `realm_warden`)

---

## 🎨 Texture Specifications

| Texture Type            | Recommended Size | Description                                                  |
| ----------------------- | ---------------- | ------------------------------------------------------------ |
| `<boss_id>_base.png`    | 256×32 px        | Row 1 (0–10px) = background, Row 2 (10–20px) = progress fill. The mod draws a 182px wide bar from these rows. |
| `<boss_id>_overlay.png` | 256×32 px        | Decorative border drawn exactly over the bar.                |

> ⚠️ If sizes differ, you may see stretching or offset issues. It’s safest to edit the default textures provided by the mod.

---

## ✨ Example: Custom “Realm Warden” Bar

1. Confirm the boss ID is `realm_warden`
2. Prepare two textures `realm_warden_base.png` and `realm_warden_overlay.png`
3. Place them in the resource pack path described above
4. Put your resource pack in `.minecraft/resourcepacks/` and activate it in-game

---

## ⚠️ Important Notes

1. **Only affects EEEAB's Mobs mod bosses**
2. **Strict namespace requirement**  
   The path must always start with `assets/eeeabsmobs/...` – you cannot use your own pack namespace.
3. **Position / color cannot be tuned via textures**  
   Offsets, text color, and vertical increments are hardcoded.
4. **Resource pack priority**  
   If multiple packs define the same boss, the last loaded pack wins. Reload resources (`F3 + T`) to apply changes.
5. **Missing textures**  
   If only `_base` is provided, the mod uses the default overlay. If both are missing, the default boss bar stays.

---

## 🛠️ Troubleshooting

| Issue               | Solution                                                     |
| ------------------- | ------------------------------------------------------------ |
| Bar doesn’t change  | Check file path and naming. Verify the boss belongs to this mod. Press `F3 + T` to reload. |
| Images misaligned   | Ensure textures are 256×32 px and the background/progress rows are exactly 10px high. |
| Game crash / no bar | Make sure the PNG is not corrupted. Remove custom textures and restart the game. |

---


# 自定义 Boss 血条教程

该功能允许你通过资源包**替换本模组中任意 Boss 的血条贴图**，让血条外观更符合你的喜好或整合包主题

---

## 📁 资源包结构

在资源包内创建以下路径：

```
assets/eeeabsmobs/textures/gui/boss_bar/
```

并在该目录下放入两张图片（按 Boss 的注册名命名）：

- `<boss_id>_base.png`   – 基础血条（可选）
- `<boss_id>_overlay.png` – 外框装饰层（可选）

> `<boss_id>` 指 Boss 的实体注册 ID（例如 `relic_annihilator`、`realm_warden`）

---

## 🎨 图片规格要求

| 图片类型                | 推荐尺寸    | 说明                                                         |
| ----------------------- | ----------- | ------------------------------------------------------------ |
| `<boss_id>_base.png`    | 256×32 像素 | 第一行（0~10px）为背景，第二行（10~20px）为进度填充。系统会按比例裁剪 182px 宽度显示 |
| `<boss_id>_overlay.png` | 256×32 像素 | 装饰外框，会完整绘制在血条上方                               |

> ⚠️ 若尺寸不符，可能出现错位或拉伸。建议基于模组默认贴图修改

---

## ✨ 示例：自定义“界域戍卫”血条

1. 确认 Boss ID 为 `realm_warden`
2. 准备两张贴图 `realm_warden_base.png` 和 `realm_warden_overlay.png`
3. 放入资源包的上述路径中
4. 将资源包放入 `.minecraft/resourcepacks/` 文件夹，并在游戏中加载

---

## ⚠️ 注意事项

1. **仅对EEEAB's Mobs模组内的 Boss 生效**
2. **资源包需使用模组命名空间**  
   路径必须固定为 `assets/eeeabsmobs/...`，不能用你的资源包命名空间。
3. **血条位置、颜色等无法通过资源包调整**  
   偏移量、文字颜色、垂直间隔等由模组代码硬编码
4. **资源包优先级**  
   若多个资源包定义了同一 Boss，最后加载的包会覆盖之前的。重新加载资源包（`F3 + T`）即可刷新血条
5. **缺失图片的处理**  
   若只提供了 `_base` 而无 `_overlay`，系统会使用默认外框。若都缺失，则保持模组默认血条

---

## 🛠️ 故障排查

| 问题              | 解决方法                                                     |
| ----------------- | ------------------------------------------------------------ |
| 血条没有变化      | 检查图片路径和命名是否正确；确认 Boss ID属于本模组；按 `F3 + T` 重载资源包 |
| 图片显示错位      | 检查图片尺寸是否为 256×32，并确认背景和进度层的高度为 10 像素一行 |
| 游戏崩溃 / 无血条 | 确保图片格式为 PNG，且未损坏。删除临时替换的图片后重启游戏   |
