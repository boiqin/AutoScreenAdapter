# AutoScreenAdapter

该项目基于今日头条屏幕适配原理，综合网上方案，提供一个较为完善的屏幕适配解决途径。
如有侵权，可联系我。

## 如何使用

#### 第一步: 添加依赖

添加以下代码到你主app目录下的`build.gradle`文件中:

```gradle
dependencies {
    implementation 'com.boiqin:AutoScreenAdapter:1.0.2'
}
```

#### 第二步: 添加适配
- 在`activity`中单独启用适配：
```
    AutoScreenAdapter.match(this, 375f)
```
- 在`activity`中取消适配：       
```
    AutoScreenAdapter.cancelMatch(this)
```
- 在`application`全局适配
```
    AutoScreenAdapter.register(this, 375f)
```

  
## 常见问题

此方案修改的`resouces`对象是全局的，因此对第三方sdk中的页面可能会有影响，如果经过验证第三方页面无影响，或者页面都是自己项目可修改的，可选取此方案


