# Ax-Chocolate Theme

- `Ax-Chocolate`는 DayNight, Day, Night 테마를 모두 지원합니다.
- `Ax-Chocolate` 라이브러리 내의 모든 디자인 컴포넌트의 속성들을 Application Theme으로 선언하면, 해당 속성들이 앱 전체에 적용됩니다.

## `AxChocolateTheme` 전체 속성

```xml
<style name="Base.Theme.XXX" parent="AxChocolateTheme.(DayNight or Day or Night)">
    <item name="chocolatePrimaryColor">my primary color</item>

    <item name="chocolate_PressEffectEnabled">true</item>
    <item name="chocolate_PressEffectStrengthLevel">normal</item>

    <item name="chocolate_CornerRadius">16dp</item>
    <item name="chocolate_RippleColor">@color/chocolate_ripple_color_black</item>
    <item name="chocolate_RippleApplyTo">foreground</item>
    <item name="chocolate_BackgroundDisabledColor">@color/chocolate_box_button_background_disabled</item>

    <!-- ChocolateTextView's attributes -->
    <item name="chocolate_TextView_HtmlTextEnabled">false</item>
    <item name="chocolate_TextView_TextColor">@color/chocolate_box_button_text</item>
    <item name="chocolate_TextView_TextDisabledColor">@color/chocolate_box_button_text_disabled</item>

    <!-- ChocolateBoxButton's attributes -->
    <item name="chocolate_BoxButton_Theme_BoxPaddingVertical">14dp</item>
    <item name="chocolate_BoxButton_Theme_BoxPaddingHorizontal">18dp</item>
    <item name="chocolate_BoxButton_TextSize">18sp</item>
    <item name="chocolate_BoxButton_TextColor">@color/chocolate_box_button_text</item>
    <item name="chocolate_BoxButton_TextDisabledColor">@color/chocolate_box_button_text_disabled</item>
    <item name="chocolate_BoxButton_TextStyle">normal</item>
    <item name="chocolate_BoxButton_TextPaddingTop">0dp</item>
    <item name="chocolate_BoxButton_TextPaddingBottom">0dp</item>
    <item name="chocolate_BoxButton_FontFamily">@null</item>
    <item name="chocolate_BoxButton_IncludeFontPadding">true</item>
    <item name="chocolate_BoxButton_IconTint">@color/chocolate_box_button_text_day</item>
    <item name="chocolate_BoxButton_IconPadding">2dp</item>
    <item name="chocolate_BoxButton_IconMarginWithText">8dp</item>

    <item name="chocolate_BoxButton_RippleColor">@color/chocolate_ripple_color_black</item>
    <item name="chocolate_BoxButton_BackgroundColor">my box button primary color</item>
    <item name="chocolate_BoxButton_BackgroundDisabledColor">@color/chocolate_box_button_background_disabled</item>
    <item name="chocolate_BoxButton_BackgroundCornerRadius">16dp</item>

    <!-- ChocolateBottomSheetDialogFragment's attributes -->
    <item name="chocolate_BottomSheet_OuterMarginEnabled">true</item>
    <item name="chocolate_BottomSheet_OuterMargin">10dp</item>
    <item name="chocolate_BottomSheet_CornerRadius">16dp</item>
    <item name="chocolate_BottomSheet_GripBarVisible">true</item>
    <item name="chocolate_BottomSheet_GripBarColor">@color/chocolate_bottom_sheet_grip_bar</item>
    <item name="chocolate_BottomSheet_BackgroundColor">@color/chocolate_bottom_sheet_background</item>
</style>
```

- Application Theme으로 설정하기

```xml
<application
    android:theme="@style/Base.Theme.XXX">
</application>
```

## `AxChocolateTheme` 속성 상세

### Chocolate Theme Attributes

| Attribute Name                       | Description (설명) | Format      | Default Value                                     |
|--------------------------------------|------------------|-------------|---------------------------------------------------|
| `chocolatePrimaryColor`              | 주요 색상            | `string`    | `my primary color`                                |
| `chocolate_PressEffectEnabled`       | 눌림 효과 활성화 여부     | `boolean`   | `true`                                            |
| `chocolate_PressEffectStrengthLevel` | 눌림 효과 강도         | `string`    | `normal`                                          |
| `chocolate_CornerRadius`             | 코너 반경            | `dimension` | `16dp`                                            |
| `chocolate_RippleColor`              | 물결 효과 색상         | `color`     | `@color/chocolate_ripple_color_black`             |
| `chocolate_RippleApplyTo`            | 물결 효과 적용 대상      | `string`    | `foreground`                                      |
| `chocolate_BackgroundDisabledColor`  | 비활성화 상태의 배경 색상   | `color`     | `@color/chocolate_box_button_background_disabled` |

### ChocolateTextView Attributes

| Attribute Name                         | Description (설명) | Format    | Default Value                               |
|----------------------------------------|------------------|-----------|---------------------------------------------|
| `chocolate_TextView_HtmlTextEnabled`   | HTML 텍스트 지원 여부   | `boolean` | `false`                                     |
| `chocolate_TextView_TextColor`         | 텍스트 색상           | `color`   | `@color/chocolate_box_button_text`          |
| `chocolate_TextView_TextDisabledColor` | 비활성화 상태의 텍스트 색상  | `color`   | `@color/chocolate_box_button_text_disabled` |

### ChocolateBoxButton Attributes

| Attribute Name                                   | Description (설명)    | Format      | Default Value                                     |
|--------------------------------------------------|---------------------|-------------|---------------------------------------------------|
| `chocolate_BoxButton_Theme_BoxPaddingVertical`   | 버튼 상하 패딩            | `dimension` | `14dp`                                            |
| `chocolate_BoxButton_Theme_BoxPaddingHorizontal` | 버튼 좌우 패딩            | `dimension` | `18dp`                                            |
| `chocolate_BoxButton_TextSize`                   | 버튼 텍스트 크기           | `dimension` | `18sp`                                            |
| `chocolate_BoxButton_TextColor`                  | 버튼 텍스트 색상           | `color`     | `@color/chocolate_box_button_text`                |
| `chocolate_BoxButton_TextDisabledColor`          | 비활성화 상태의 버튼 텍스트 색상  | `color`     | `@color/chocolate_box_button_text_disabled`       |
| `chocolate_BoxButton_TextStyle`                  | 버튼 텍스트 스타일          | `string`    | `normal`                                          |
| `chocolate_BoxButton_TextPaddingTop`             | 버튼 텍스트 상단 패딩        | `dimension` | `0dp`                                             |
| `chocolate_BoxButton_TextPaddingBottom`          | 버튼 텍스트 하단 패딩        | `dimension` | `0dp`                                             |
| `chocolate_BoxButton_FontFamily`                 | 버튼 텍스트 폰트 패밀리       | `reference` | `@null`                                           |
| `chocolate_BoxButton_IncludeFontPadding`         | 버튼 텍스트의 폰트 패딩 포함 여부 | `boolean`   |                                                   |
| `chocolate_BoxButton_IconTint`                   | 버튼 아이콘 색상 틴트        | `color`     | `@color/chocolate_box_button_text_day`            |
| `chocolate_BoxButton_IconPadding`                | 버튼 아이콘 패딩           | `dimension` | `2dp`                                             |
| `chocolate_BoxButton_IconMarginWithText`         | 버튼 아이콘과 텍스트 간 여백    | `dimension` | `8dp`                                             |
| `chocolate_BoxButton_RippleColor`                | 버튼 물결 효과 색상         | `color`     | `@color/chocolate_ripple_color_black`             |
| `chocolate_BoxButton_BackgroundColor`            | 버튼 배경 색상            | `string`    | `my box button primary color`                     |
| `chocolate_BoxButton_BackgroundDisabledColor`    | 비활성화 상태의 버튼 배경 색상   | `color`     | `@color/chocolate_box_button_background_disabled` |
| `chocolate_BoxButton_BackgroundCornerRadius`     | 버튼 배경의 코너 반경        | `dimension` | `16dp`                                            |

### ChocolateBottomSheetDialogFragment Attributes

| Attribute Name                             | Description (설명) | Format      | Default Value                              |
|--------------------------------------------|------------------|-------------|--------------------------------------------|
| `chocolate_BottomSheet_OuterMarginEnabled` | 외부 마진 활성화 여부     | `boolean`   | `true`                                     |
| `chocolate_BottomSheet_OuterMargin`        | 외부 마진 크기         | `dimension` | `10dp`                                     |
| `chocolate_BottomSheet_CornerRadius`       | 코너 반경            | `dimension` | `16dp`                                     |
| `chocolate_BottomSheet_GripBarVisible`     | 그립 바 표시 여부       | `boolean`   | `true`                                     |
| `chocolate_BottomSheet_GripBarColor`       | 그립 바 색상          | `color`     | `@color/chocolate_bottom_sheet_grip_bar`   |
| `chocolate_BottomSheet_BackgroundColor`    | 배경 색상            | `color`     | `@color/chocolate_bottom_sheet_background` |