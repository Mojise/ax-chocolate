# ChocolateBoxButton

## ChocolateView

### attributes

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


## `ChocolateTextView`

### attributes

| Attribute Name                         | Description         | Format    | Values          |
|----------------------------------------|---------------------|-----------|-----------------|
| `chocolate_TextView_HtmlTextEnabled`   | HTML 형식의 텍스트를 지원 여부 | `boolean` | `true`, `false` |
| `chocolate_TextView_TextColor`         | 텍스트 색상              | `color`   |                 |
| `chocolate_TextView_TextSelectedColor` | 텍스트가 선택되었을 때의 색상    | `color`   |                 |
| `chocolate_TextView_TextDisabledColor` | 텍스트가 비활성화되었을 때의 색상  | `color`   |                 |

## `ChocolateBoxButton`

https://github.com/user-attachments/assets/2ba7d908-5003-4cf0-a9d2-d7d9814849cc

### attributes

- `ChocolateBoxButton`에서 사용 가능한 ChocolateView 공통 속성은 `chocolate_PressEffectEnabled`, `chocolate_PressEffectScaleRatio`, `chocolate_PressEffectStrengthLevel` 이다.

| Attribute Name                                | Description          | Format      | Values                                        |
|-----------------------------------------------|----------------------|-------------|-----------------------------------------------|
| `chocolate_BoxButton_Text`                    | 버튼에 표시될 텍스트          | `string`    |                                               |
| `chocolate_BoxButton_TextSize`                | 버튼 텍스트의 크기           | `dimension` |                                               |
| `chocolate_BoxButton_TextColor`               | 버튼 텍스트의 색상           | `color`     |                                               |
| `chocolate_BoxButton_TextDisabledColor`       | 버튼이 비활성화되었을 때 텍스트 색상 | `color`     |                                               |
| `chocolate_BoxButton_TextStyle`               | 버튼 텍스트의 스타일          | `enum`      | **`normal`**, `bold`, `italic`, `bold_italic` |
| `chocolate_BoxButton_FontFamily`              | 버튼 텍스트의 폰트 패밀리       | `reference` |                                               |
| `chocolate_BoxButton_IncludeFontPadding`      | 버튼 텍스트의 폰트 패딩 포함 여부  | `boolean`   |                                               |
| `chocolate_BoxButton_TextPaddingTop`          | 버튼 텍스트의 상단 패딩        | `dimension` |                                               |
| `chocolate_BoxButton_TextPaddingBottom`       | 버튼 텍스트의 하단 패딩        | `dimension` |                                               |
| `chocolate_BoxButton_Icon`                    | 버튼에 표시될 아이콘          | `reference` |                                               |
| `chocolate_BoxButton_IconTint`                | 버튼 아이콘의 색상 틴트        | `color`     |                                               |
| `chocolate_BoxButton_IconSize`                | 버튼 아이콘의 크기           | `dimension` |                                               |
| `chocolate_BoxButton_IconPadding`             | 버튼 아이콘 주변의 패딩        | `dimension` |                                               |
| `chocolate_BoxButton_IconMarginWithText`      | 버튼 아이콘과 텍스트 간 여백     | `dimension` |                                               |
| `chocolate_BoxButton_StrokeWidth`             | 버튼 테두리의 두께           | `dimension` |                                               |
| `chocolate_BoxButton_StrokeColor`             | 버튼 테두리의 색상           | `color`     |                                               |
| `chocolate_BoxButton_StrokeDisabledColor`     | 버튼 비활성화 시 테두리 색상     | `color`     |                                               |
| `chocolate_BoxButton_StrokeSelectedColor`     | 버튼 선택 시 테두리 색상       | `color`     |                                               |
| `chocolate_BoxButton_RippleColor`             | 버튼 클릭 시 물결 효과의 색상    | `color`     |                                               |
| `chocolate_BoxButton_RippleSelectedColor`     | 버튼 선택 시 물결 효과의 색상    | `color`     |                                               |
| `chocolate_BoxButton_BackgroundColor`         | 버튼 배경 색상             | `color`     |                                               |
| `chocolate_BoxButton_BackgroundSelectedColor` | 버튼 선택 시 배경 색상        | `color`     |                                               |
| `chocolate_BoxButton_BackgroundDisabledColor` | 버튼 비활성화 시 배경 색상      | `color`     |                                               |
| `chocolate_BoxButton_BackgroundCornerRadius`  | 버튼 배경의 코너 반경         | `dimension` |                                               |
| `chocolate_BoxButton_IconPosition`            | 버튼 아이콘의 위치           | `enum`      | **`left`**, `right`                           |
| `chocolate_BoxButton_ChainStyle`              | 버튼 레이아웃 체인의 스타일      | `enum`      | **`packed`**, `spread`, `spread_inside`       |
| `chocolate_BoxButton_Gravity`                 | 버튼 내용물의 정렬           | `enum`      | **`center`**, `start`, `end`                  |
