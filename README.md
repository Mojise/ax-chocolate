# Ax-Chocolate

## 라이브러리 개요

[![](https://jitpack.io/v/mojise/ax-chocolate.svg)](https://jitpack.io/#mojise/ax-chocolate)

- `Ax-Chocolate`는 AX 개발팀에서 사용하는 공통 안드로이드 디자인 시스템 라이브러리입니다.
- 여러 앱을 개발하고 유지보수하는 과정에서 공통으로 사용되는 다이얼로그, 로딩, 버튼 등의 디자인 요소들을 라이브러리로 제공합니다.
- 또한, 디자인 혹은 뷰와 관련된 편의 기능 함수들을 제공합니다.
- `Ax-Chocolate`는 DayNight, Day, Night 테마를 모두 지원합니다.
- `Ax-Chocolate`는 각각의 앱마다 조금씩 다른 디자인 특성을 고려하여 커스터마이징이 가능하며, 이를 위해 Application Theme으로 사용할 수 있는 `AxChocolateTheme`을 제공합니다.

## 라이브러리 설정

### Step 1. Jitpack 저장소 추가

- `settings.gradle.kts`

```kotlin
pluginManagement {
    repositories {
        // ...
        maven("https://www.jitpack.io")
    }
}
```

### Step 2. 의존성 추가

- `libs.versions.toml`
```toml
ax-chocolate = { group = "com.github.mojise", name = "ax-chocolate", version = "0.0.5-beta" }
```
- `build.gradle.kts`
```kotlin
dependencies {
    implementation(libs.ax.chocolate)
}
```

### Step 3. Application Theme 설정

```xml
<style name="Base.Theme.XXX" parent="AxChocolateTheme.(DayNight or Day or Night)">
    <!-- Ax-Chocolate 디자인 시스템 속성 정의 -->
    <item name="chocolatePrimaryColor">my primary color</item>
</style>
```
- `AndroidManifest.xml`

```xml
<application
    android:theme="@style/Theme.AxChocolateTest">
</application>
```


## 기본 사용법


### ChocolateBoxButton

- `res/values/themes.xml`

```xml
<com.mojise.library.chocolate.button.box.ChocolateBoxButton
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:chocolate_BoxButton_Text="초콜릿 버튼 with 아이콘"
    app:chocolate_BoxButton_Icon="@drawable/icon_notification"/>
```


## 상세 사용법

- [Ax-Chocolate Theme](docs/Guide_AxChocolate_Theme.md)
- [ChocolateView](docs/Guide_ChocolateView.md)
- [ChocolateBoxButton](docs/Guide_ChocolateBoxButton.md)
