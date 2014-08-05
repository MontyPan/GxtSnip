自己寫的 composite 要自動調整大小，
要注意是 extend GXT 的 `com.sencha.gxt.widget.core.client.Composite`，
而不是 GWT 的 `com.google.gwt.user.client.ui.Composite`

在 ui.xml 下，要知道某個 widget 下可以卡哪些特殊 tag，就去挖 source code，看看有沒有 `@UiChild`。
例如 `ContentPanel` 有 `addButton()` 跟 `addTool()` 掛 `@UiChild`，所以就可以這樣寫

	<c:ContentPanel>
		<c:button><f:Foo /></c:button>
		<c:tool><f:Foo /></c:tool>
	</c:ContentPanel>

因為 GXT 的 component 都是這樣處理的（GWT 反而不是... WTF），
所以當然就可以自動建立清單啦... 參見 [UiChildList.md](UiChildList.md)。

`FooTabPanel` extends `TabPanel` 不明瞭為什麼這樣還是會去 call 到 `TabPanel.add()`：

	//in FooTabPanel
	@Override
	@UiChild(tagname="tab")
	public void add(IsWidget widget, TabItemConfig config) {
		Window.alert("不會出現的訊息");
	}

	//in ui.xml
	<foo:FooTabPanel>
		<foo:tab><b:TextButton text="FOO" /></foo:tab>
	</foo:FooTabPanel>

如果 method 名字改成不是 `add()` 就沒問題...... 


Layout Container 系
-------------------
有在[官方文件](http://docs.sencha.com/gxt-guides/3/ui/layout/LayoutContainers.html)的才算，
是說 `TabPanel` 居然不算... ＝＝?


### FieldLabel ###
`text` 跟 `widget` 的間隔距離始終是個謎...... WTF


### SimpleContainer ###
會自動讓小孩的大小跟自己的大小一樣


### ContentPanel ###
在 ui.xml 當中作 contentPanel.addButton() 的方法：

	<core:ContentPanel>
		<core:button>
			<foo:Foo />
		<core:button>
		<core:button>
			<foo:Foo />
		<core:button>		
	</core:ContentPanel>

原來一個 `<core:button>` 只能塞一個 widget，我之前都誤會它了 [炸]。
反過來說，`@UiChild` 的邏輯還要更深入的瞭解... (艸


### Dialog ###
要知道使用者對 `Dialog` 做了什麼事情，是掛 `addDialogHideHandler()` 這個 handler。
從 `DialogHideEvent.getHideButton()` 可以知道是按下哪個 button。
不知道該說巧妙還是該說 WTF...... Orz


### AccordionLayoutContainer ###
小孩只能是 `ContentPanel`，而且（應該說「所以」？）
`ContentPanel` 還能塞 `AccordionLayoutAppearance` 來改變 `ContentPanel` 的外觀。 


### HBox / VBox ###
做了 `setPack()`，會發現小孩的 layoutData 不管怎麼設定 flex 值，
都不會有預其中的大小變化，而是以小孩原本的大小呈現。
目前這是實驗結果，還沒追 source code [死]。


### HorizontalLayoutContainer / VerticalLayoutContainer ###

#### HorizontalLayoutData / VerticalLayoutData ####
假設 `HorizontalLayoutContainer` 的寬度是 100，它的小孩有設定 `setLayoutData(ld)`，
`ld` 有作 `ld.setWidth(x)`，則小孩的寬度會是：

* x > 1：小孩寬度會是 `x`
* 0 > x >= 1：小孩寬度會是 `100 * x`
* x = -1：不管爸爸的大小，小孩自己的原本的寬度多大就多大（容易錯，使用注意）
* x < -1：小孩寬度會是 `100 + x`。

______________________________________________________________________

一般 widget
----------

### TabPanel ###
在 ui.xml 當中只能這樣寫

	<ui:with field="tabPanelConfig" type="com.sencha.gxt.widget.core.client.TabItemConfig">
		<ui:attributes text="tab 名字" />
	</ui:with>
	<gxt:TabPanel>
		<gxt:child config="tabPanelConfig">
			<foo:Foo />
		</gxt:child>
	</gxt:TabPanel>

有點討厭，不過就算自己設計好像也沒辦法改變什麼 Orz


### Grid ###
`setView(GridView)` 可以設定一些... 還不確定可以幹麼的東西 [喂喂]。
其中 `GridView.setForceFit()`，如果設定 `true` 則 Grid 不會出現 scroll bar；
若有多個 column、縮小其中一個 column，則其他 column 會變大補滿（不確定演算法）。


#### AbstractGridEditing ####
`setEditableGrid()` 如果傳入 null 值是表示移除 edit 的效果，
因為一開始會作 `groupRegistration.removeHandler()`，然後在傳入值不為 null 才會掛相關 handler。
這在初始化設定（尤其搭配 ui.xml）時要特別注意 [淚目]


### ComboBox ###
`setTriggerAction(TriggerAction.ALL)` 可以在使用者再次要求下拉選單時顯示所有值。
像 `TimeField` 就非常需要... Orz

要注意 ComboBox 的顯示值（或著說 `LabelProvider.getLabel()` 的回傳值）不能是 `\n` 結尾。
顯示上是不會有問題、也會觸發 `SelectionEvent`，
但是在 onblur 的時候不會觸發 `ValueChangeEvent`、ComboBox 的值會消失。


### LabelToolItem ###
`LabelToolItem` 轉換成 DOM 就只是一個 `div` 包一個字串，當然有掛一些 css（沒有特別的內容），
除了 package name 很奇怪之外，基本上應該可以視為 GXT 版的 label。
（謎之聲：之前測試不知道是測試到哪裡去了... (艸  ）

______________________________________________________________________

store & value provider
----------------------
store 就是資料來源，value provider 就是決定顯示 vo 資料的方法

ui.xml 裡頭一定只能

	<ui:with type="com.sencha.gxt.data.shared.TreeStore" field="store" />
	<ui:with type="com.sencha.gxt.core.client.ValueProvider" field="valueProvider" />
	
不能

	<ui:with type="foo.MyTreeStore" field="store" />
	<ui:with type="foo.MyValueProvider" field="valueProvider" />

甚至宣告時 

	//正常
	@UiField(provider=true)	TreeStore<Foo> store = new MyTreeStore();
	
	//不行
	@UiField(provider=true)	MyTreeStore store = new MyTreeStore();

幹這什麼黑魔法。

______________________________________________________________________

DrawComponent
-------------
可以視為 GXT 的 canvas，更正確地說是 canvas container / wrapper。
實際上的 canvas 是 `Surface`，會用 deferred binding 的方法抽換實做，
目前大抵上預設是 `SVG`，遇到 IE6～8 會替換成 `VML`，
另外還有隱藏實驗版（JavaDoc 找不到）的 `Canvas2d` 版。
因為 wrapper 過，所以基本上可以不用理會底層實做，GXT 會提供一個一致的 API。

目前單純顯示，可做出想要的效果。
但是一旦搭配有 resize 機制的 GXT container（例如祖先有 `Viewport`），
畫面上看起來就會一片空白（但是大小還在）... 還在追原因中 [淚目]


## Chart ##
`chart.redrawChart()` 之類的繪圖時刻如果炸 `java.lang.NegativeArraySizeException` 之類狀況，
先檢查一下 chart 的大小是否合理。如果 chart 沒有大小（1 * 1），那麼炸 exception 好像也很合理。

`NumericAxis.clear()`（實際是 `CartesianAxis.clear()`）不會清 fields，
但是不清 fields 好像也不會怎樣？細節不明 Orz


### TimerAxis ###
如果用 `TimeAxis` 作 X 軸（不確定作 Y 軸會怎樣 XD），
直接設定 `setStartDate()` 跟 `setEndDate()` 就會幫你過濾掉不在時間範圍內的 data，
不用自己整理 store。

`TimerAxis` 的時間區間過濾機制會影響 chart 的 `getCurrentStore()`（`substore`）的值。
這在「清空 / 重塞 store、又變更其他 axis」的前提下，`chart.redrawChart()` 會炸。
因為 render 實際處理的是 `getCurrentStore()`，如果舊的 store 格式跟新的 axis 格式對不上，就會出錯。
解法是在變更其他 axis 之後作 `TimerAxis.drawAxis(false)`（傳入 true / false 好像沒差），
在 `drawAxis()` 裡頭的 `applyData()`（`TimeAxis` 有 override）會重新計算 / 設定 `currentStore`。
當然這有點浪費，因為在 `chart.render()` 裡頭其實會對各個 axis 作 `drawAxis(false)`，
只能說理論上沒辦法控制 `TimerAxis` 一定要先作，`applyData()` 是 protected 所以沒辦法直接呼叫
前提觸發的條件又不是很 general，只好覆蓋新鮮的肝臟，結束這一回合 T__T。


### Legend ###
`Legend` 的資料來源是從 `Series.setYField()` 傳入的 `ValueProvider` 的 `getPath()`。

______________________________________________________________________

DnD
---
`new DropTarget(foo)` 會讓 `foo` 變成可 drop 的對象，
囧點在於他並不會管 drag 的來源、預設所有的 DnD 行為都收，
必須自己去排除哪些 drag 來源是不想處理的... 有點麻煩阿...... Orz
