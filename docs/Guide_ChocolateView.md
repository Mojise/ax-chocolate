# ChocolateView

- 사용자의 터치 이벤트에 대한 피드백을 제공하는 안드로이드 기본 뷰

## ChocolateView 목록

- [ChocolateCardView.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateCardView.kt)
- [ChocolateConstraintLayout.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateConstraintLayout.kt)
- [ChocolateFloatingActionButton.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateFloatingActionButton.kt)
- [ChocolateFrameLayout.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateFrameLayout.kt)
- [ChocolateImageView.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateImageView.kt)
- [ChocolateLinearLayout.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateLinearLayout.kt)
- [ChocolateMaterialCardView.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateMaterialCardView.kt)
- [ChocolateShapeableImageView.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateShapeableImageView.kt)
- [ChocolateTextView.kt](../ax-chocolate/src/main/java/com/mojise/library/chocolate/view/ChocolateTextView.kt)

## Attributes

| Attribute Name                       | Description                | Format      | Values                                                  |
|--------------------------------------|----------------------------|-------------|---------------------------------------------------------|
| `chocolate_PressEffectEnabled`       | 버튼 클릭 시 눌림 효과를 활성화 또는 비활성화 | `boolean`   |                                                         |
| `chocolate_PressEffectScaleRatio`    | 눌림 효과의 확대/축소 비율            | `float`     | 0 ~ 1                                                   |
| `chocolate_PressEffectStrengthLevel` | 눌림 효과의 강도 수준               | `enum`      | `soft`, `normal`, `deep`, `deep_strong`, `deep_extreme` |
| `chocolate_CornerRadius`             | 뷰의 코너 반경                   | `dimension` |                                                         |
| `chocolate_RippleColor`              | 클릭 시 물결 효과의 색상             | `color`     |                                                         |
| `chocolate_RippleSelectedColor`      | 선택된 상태에서 물결 효과의 색상         | `color`     |                                                         |
| `chocolate_RippleApplyTo`            | 물결 효과를 적용할 영역              | `enum`      | `foreground`, `background`                              |
| `chocolate_StrokeWidth`              | 뷰 테두리의 두께                  | `dimension` |                                                         |
| `chocolate_StrokeColor`              | 뷰 테두리의 색상                  | `color`     |                                                         |
| `chocolate_StrokeDisabledColor`      | 비활성화 상태에서 뷰 테두리의 색상        | `color`     |                                                         |
| `chocolate_StrokeSelectedColor`      | 선택된 상태에서 뷰 테두리의 색상         | `color`     |                                                         |
| `chocolate_BackgroundColor`          | 뷰 배경의 색상                   | `color`     |                                                         |
| `chocolate_BackgroundSelectedColor`  | 선택된 상태에서 뷰 배경의 색상          | `color`     |                                                         |
| `chocolate_BackgroundDisabledColor`  | 비활성화 상태에서 뷰 배경의 색상         | `color`     |                                                         |

## Special attributes

### `ChocolateTextView`'s attributes

| Attribute Name                         | Description         | Format    | Values |
|----------------------------------------|---------------------|-----------|--------|
| `chocolate_TextView_HtmlTextEnabled`   | HTML 형식의 텍스트를 지원 여부 | `boolean` |        |
| `chocolate_TextView_TextColor`         | 텍스트 색상              | `color`   |        |
| `chocolate_TextView_TextSelectedColor` | 텍스트가 선택되었을 때의 색상    | `color`   |        |
| `chocolate_TextView_TextDisabledColor` | 텍스트가 비활성화되었을 때의 색상  | `color`   |        |