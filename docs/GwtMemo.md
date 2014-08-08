compatible 規範
---------------
符合下列條件的 class 才能在 GWT 內使用：

* primitive data type
* array（陣列元素也必須 compatible）
* enum
* JRE emulation class（包含可用 method）
* 繼承 compatible class
* 所有 field 都符合 compatible 原則

另外，`synchronized` 與 `finalize()` 加了不會出問題，但是 GWT 會直接忽略。

_（沒有考慮撰寫 emulate class 的招數）_

reference：

* JRE compatibility：http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsCompatibility.html
* JRE emulation class： http://www.gwtproject.org/doc/latest/RefJreEmulation.html


### RPC（serializable）規範 ###

* 滿足 compatible 規範
* implement `IsSerializable` 或是 `Serializable`
* 有 default（沒有參數）constructor，任何 access modifier 均可（private 也無所謂）。
	或是根本沒有任何 constructor。
* 所有 field 都符合 RPC 原則。
	* 例外：final field（不會在 GWT RPC 中傳遞）
	* 例外：transient field（接收端會得到 null）

_（沒有考慮 custom serialization 的招數）_

備註：enum 只傳遞名稱。例如 `FooEnum.FOO`，就只會傳 `FOO`，裡頭的 field 值一概忽略。

reference：

* RPC： http://www.gwtproject.org/doc/latest/DevGuideServerCommunication.html#DevGuideSerializableTypes


Image
-----
用 `ImageResource` 的 `Image` 如果要調整大小，

	new Image(imgResource);	//size 太大會留白
	new Image(imgResource.getSafeUri());	//OK


UiBinder
--------
用 UiBinder 弄畫面，執行時一直炸找不到 fooWidget class 的錯誤（但是 Eclipse 沒有 compile error），
先單純用程式 new FooWidget() 出來，通常就會知道 FooWidget 錯在哪裡了。
（2.5.1 時常見不小心用了 generic 的 `<>` 就炸了，但是 Eclipse 不會炸錯誤 Orz）


### ui:import ###
似乎是 ui.xml 中可以使用 enum 的唯一方法
（[ui:import ref](http://stackoverflow.com/questions/9492658/can-i-use-enum-values-as-field-values-inside-uibinder-template)）。


無法分類
--------
如果在 deploy / online 階段 RPC 突然出問題，server side 帳面上的 exception 是

> Exception while dispatching incoming RPC call com.google.gwt.user.client.rpc.SerializationException: 
> Type 'wtf.client.Foo' was not assignable to 'com.google.gwt.user.client.rpc.IsSerializable' and did not have a custom field serializer.
> For security purposes, this type will not be serialized.

如果往回找 log，會發現更之前就會炸了一個 exception，是找不到 `XXXX.gwt.rpc` 的檔案。
這種情況，就是 client side（不知道為什麼）cache 住前一個版本（GWT compile 產出的）JS。
理論上清空 browser cache（或是按下 Ctrl + F5），就會正常。

至於 dev mode 階段，說真的，我還沒遇到過 [合掌]


GWT 活跳跳的證據
----------------
* 2011 年
	* https://plus.google.com/+RayCromwell/posts/ivVepvxCu3g  
		Ray Cromwell 列出使用 GWT 的 Google Product。
* 2013 年：
	* https://plus.google.com/+ThomasBroyer/posts/iCq9E2Wk3Jz
		新版的 Google Drive 的 Sheet 使用 GWT 撰寫
	* http://www.slideshare.net/cromwellian1/gwtcreate-keynote-san-francisco  
		Ray Cromwell 在 Gwt.Create 報告 Google Drive 使用 GWT 撰寫的投影片（p.6）

註：Ray Cromwell 是 Googler、現任 GWT 委員長。