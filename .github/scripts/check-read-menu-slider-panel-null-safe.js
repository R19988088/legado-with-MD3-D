const fs = require("fs");

const source = fs.readFileSync(
  "app/src/main/java/io/legado/app/ui/book/read/ReadMenu.kt",
  "utf8"
);

const unsafePatterns = [
  /\bsliderPanel\.setCardBackgroundColor\(/,
  /\bsliderPanel\.cardElevation\s*=/,
  /\bsliderPanel\.radius\s*=/,
  /\bbinding\.sliderPanel\.isVisible\s*=/,
];

const failures = unsafePatterns
  .filter((pattern) => pattern.test(source))
  .map((pattern) => pattern.toString());

if (failures.length) {
  console.error(
    "ReadMenu sliderPanel is nullable because layout-land/view_read_menu.xml omits it. Use safe calls."
  );
  failures.forEach((pattern) => console.error(`Unsafe pattern: ${pattern}`));
  process.exit(1);
}
