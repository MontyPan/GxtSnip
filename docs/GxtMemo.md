自己寫的 composite 要自動調整大小，
要注意是 extend GXT 的 `com.sencha.gxt.widget.core.client.Composite`，
而不是 GWT 的 `com.google.gwt.user.client.ui.Composite`

在 ui.xml 下，要知道某個 widget 下可以卡哪些特殊 tag，就去挖 source code，看看有沒有 `@UiChild`。
例如 `ContentPanel` 有 `addButton()` 跟 `addTool()` 掛 `@UiChild`，所以就可以這樣寫

	<c:ContentPanel>
		<c:button><f:Foo /></c:button>
		<c:tool><f:Foo /></c:tool>
	</c:ContentPanel>


### SimpleContainer ###
會自動讓小孩的大小跟自己的大小一樣


### ContentPanel ###
在 ui.xml 當中作 contentPanel.addButton() 的方法：

	<core:ContentPanel>
		<core:button>
			<!-- 只能一個 widget -->
		<core:button>
	</core:ContentPanel>


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


## layout data ##

### HorizontalLayoutContainer.HorizontalLayoutData ###
Returns the width specification. Values greater than 1 represent width in pixels. Values between 0 and 1 (inclusive) represent a percent of the width of the container. A value of -1 represents the default width of the associated widget. Values less than -1 represent the width of the container minus the absolute value of the widget width.

______________________________________________________________________

## store & value provider ##
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

## Chart ##
`chart.redrawChart()` 之類的繪圖時刻如果炸 `java.lang.NegativeArraySizeException` 之類狀況，
先檢查一下 chart 的大小是否合理。如果 chart 沒有大小（1 * 1），那麼炸 exception 好像也很合理。


### Legend ###
`Legend` 的資料來源是從 `Series.setYField()` 傳入的 `ValueProvider` 的 `getPath()`。

______________________________________________________________________

## DnD ##
`new DropTarget(foo)` 會讓 `foo` 變成可 drop 的對象，
囧點在於他並不會管 drag 的來源、預設所有的 DnD 行為都收，
必須自己去排除哪些 drag 來源是不想處理的... 有點麻煩阿...... Orz
