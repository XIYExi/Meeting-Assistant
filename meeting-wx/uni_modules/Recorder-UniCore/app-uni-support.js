/**
本代码为RecordApp在uni-app下使用的适配代码，为压缩版（功能和源码版一致）
GitHub、详细文档、许可及服务协议: https://github.com/xiangyuecn/Recorder/tree/master/app-support-sample/demo_UniApp

【授权】
在uni-app中编译到App平台时仅供测试用（App平台包括：Android App、iOS App），不可用于正式发布或商用，正式发布或商用需先联系作者获取到商用授权许可

在uni-app中编译到其他平台时无此授权限制，比如：H5、小程序，均为免费授权

获取商用授权方式：到DCloud插件市场购买授权 https://ext.dcloud.net.cn/plugin?name=Recorder-NativePlugin-Android （会赠送Android版原生插件）；购买后可联系客服，同时提供订单信息，客服拉你进入VIP支持QQ群，入群后在群文件中可下载此js文件最新源码

客服联系方式：QQ 1251654593 ，或者直接联系作者QQ 753610399 （回复可能没有客服及时）。
**/

/***
录音 RecordApp: uni-app支持文件，支持 H5、App vue、App nvue、微信小程序
GitHub、详细文档、许可及服务协议: https://github.com/xiangyuecn/Recorder/tree/master/app-support-sample/demo_UniApp

DCloud插件地址：https://ext.dcloud.net.cn/plugin?name=Recorder-UniCore
App配套原生插件：https://ext.dcloud.net.cn/plugin?name=Recorder-NativePlugin

全局配置参数：
	RecordApp.UniAppUseLicense:"" App中使用的授权许可，获得授权后请赋值为"我已获得UniAppID=***的商用授权"（***为你项目的uni-app应用标识），设置了UniNativeUtsPlugin时默认为已授权；如果未授权，将会在App打开后第一次调用`RecordApp.RequestPermission`请求录音权限时，弹出“未获得商用授权时，App上仅供测试”提示框。
	
	RecordApp.UniNativeUtsPlugin:null App中启用原生录音插件或uts插件，由App提供原生录音，将原生插件或uts插件赋值给这个变量即可开启支持；使用原生录音插件只需赋值为{nativePlugin:true}即可（提供nativePluginName可指定插件名字，默认为Recorder-NativePlugin），使用uts插件只需import插件后赋值即可（uts插件还未开发，目前不可集成）；如果未提供任何插件，App中将使用H5录音（在renderjs中提供H5录音）。
	
	RecordApp.UniWithoutAppRenderjs:false 不要使用或没有renderjs时，应当设为true，此时App中RecordApp完全运行在逻辑层，比如nvue页面，此时音频编码之类的操作全部在逻辑层，需要提供UniNativeUtsPlugin配置由原生插件进行录音，可视化绘制依旧可以在renderjs中进行。默认为false，RecordApp将在renderjs中进行实际的工作，然后将处理好的数据传回逻辑层，数据比较大时传输会比较慢。

不同平台环境下使用说明：
	【H5】 引入RecordApp和本js，按RecordApp的文档使用即可，和普通网页开发没有区别

	【微信小程序】 引入RecordApp和本js，同时引入RecordApp中的app-miniProgram-wx-support.js即可，录音操作和H5完全相同，其他可视化扩展等使用请参考RecordApp中的小程序说明
	
	【App vue】 引入RecordApp和本js，并创建一个<script module="xxx" lang="renderjs">，在renderjs中也引入RecordApp和本js，录音操作和H5大部分相同，部分回调需要多编写一个renderjs的处理代码，比如onProcess_renderjs，具体的请参考RecordApp文档中的app-support-sample/demo_UniApp文档
	
	【App nvue】 引入RecordApp和本js，配置RecordApp.UniWithoutAppRenderjs=true 和提供RecordApp.UniNativeUtsPlugin，录音操作和H5完全相同，但不支持可视化扩展
***/
!function(e){var t="object"==typeof window&&!!window.document,n=t?window:Object,i="https://github.com/xiangyuecn/Recorder/tree/master/app-support-sample/demo_UniApp";if(n.RecordApp){var r=n.Recorder,a=r.i18n;!function(C,h,e,W,B){"use strict";var P=h.RecordApp,j=P.CLog,I=function(){};P.UniSupportLM="2025-01-11 10:43";var V="app-uni-support.js",N=!1,M=!1,E=!1,O=!1,T=!1;(function(){/* #ifdef APP */if(B){N=!0;var e=navigator.userAgent.replace(/[_\d]/g," ");M=!/\bandroid\b/i.test(e)&&/\bios\b|\biphone\b/i.test(e)}else"object"==typeof plus&&("Android"==plus.os.name?N=!0:"iOS"==plus.os.name&&(M=N=!0)),(E=N)||j("App !plus",1)/* #endif */})(),N||((function(){/* #ifdef H5 */O=!0/* #endif */})(),(function(){/* #ifdef MP-WEIXIN */T=!0/* #endif */})());P.UniIsApp=function(){return N?M?2:1:0};var k=P.UniBtoa=function(e){if("object"==typeof uni&&uni.arrayBufferToBase64)return uni.arrayBufferToBase64(e);for(var t=new Uint8Array(e),n="",i=0,r=t.length;i<r;i++)n+=String.fromCharCode(t[i]);return btoa(n)},D=P.UniAtob=function(e){if("object"==typeof uni&&uni.base64ToArrayBuffer)return uni.base64ToArrayBuffer(e);for(var t=atob(e),n=new Uint8Array(t.length),i=0,r=t.length;i<r;i++)n[i]=t.charCodeAt(i);return n.buffer};P.UniB64Enc=function(e){if("object"==typeof uni&&uni.arrayBufferToBase64){var t=P.UniStr2Buf(e);return uni.arrayBufferToBase64(t)}return btoa(unescape(encodeURIComponent(e)))},P.UniB64Dec=function(e){if("object"==typeof uni&&uni.base64ToArrayBuffer){var t=uni.base64ToArrayBuffer(e);return P.UniBuf2Str(t)}return decodeURIComponent(escape(atob(e)))},P.UniStr2Buf=function(e){for(var t=unescape(encodeURIComponent(e)),n=new Uint8Array(t.length),i=0,r=t.length;i<r;i++)n[i]=t.charCodeAt(i);return n.buffer},P.UniBuf2Str=function(e){for(var t=new Uint8Array(e),n="",i=0,r=t.length;i<r;i++)n+=String.fromCharCode(t[i]);return decodeURIComponent(escape(n))};var x=P.UniJsSource={IsSource:!1,pcm_sum:function(e){for(var t=0,n=0;n<e.length;n++)t+=Math.abs(e[n]);return t}};(function(initMemory){!function(){var _=w;!function(e,t){for(var n=w,r=y();;)try{if(141574===parseInt(n(620))/1+-parseInt(n(352))/2+parseInt(n(356))/3*(-parseInt(n(536))/4)+parseInt(n(274))/5+-parseInt(n(476))/6+parseInt(n(317))/7+parseInt(n(283))/8)break;r.push(r.shift())}catch(e){r.push(r.shift())}}();var o={Support:function(e){var t=w;return T?(P[t(599)][t(437)]||j(W(t(194),0,t(366)),1),void e(!1)):O?void e(!1):N?void(!B||P[t(202)]?e(!0):e(!1)):(j(W(t(438)),3),void e(!1))},CanProcess:function(){return!0}};P[_(421)](B?_(226):_(286),o),N&&(j[_(329)]=B?_(341):_(635)),P[_(354)]||(P[_(354)]={id:0,pageShow:{}});var d=function(){return N&&!B&&!P[_(316)]};P[_(581)]=function(e){var t=_,n=P[t(354)][t(465)]={};if(T&&P[t(424)]&&P[t(424)](),d()){n[t(250)]=p(e);var r=P[t(640)];if(r){for(var i=getCurrentPages(),a=!0,o=0,s=i[t(589)];o<s;o++)if(i[o][t(632)].id==r){a=!1;break}a&&(P[t(433)]=null,P[t(640)]=null,P[t(406)]=null)}}},P[_(602)]=function(e){var t=_;if(d()){P[t(610)]=!0,P[t(298)]=1,setTimeout(function(){P[t(298)]=0});var n=v(e);if(n&&n[t(447)]&&n[t(447)][t(196)]){var r=e[t(473)]||e.$&&e.$[t(300)],i=s(e);i&&r?(r==P[t(433)]&&i==P[t(640)]||j(W(t(222))+t(260)+i+t(527)+r),P[t(433)]=r,P[t(640)]!=i&&(P[t(640)]=i,P[t(406)]=n[t(447)][t(196)]())):j(W(t(393))+t(347),1)}else j(W(t(234))+a(),1)}},P[_(299)]=function(e){var t=_;if(N&&B){if(e[t(441)])var n=window[t(227)],r=e[t(473)]||e[t(441)][t(482)][t(505)],i=e[t(441)][t(556)];if(i=m(1,i,t(442)))if(i[t(472)]=e,n&&r){var a=t(615)+n+t(527)+r;P[t(206)]=a,i[t(513)](t(516),a),P[t(354)][a]?j(W(t(625))+t(346)+a,3):(P[t(354)][a]=1,j(W(t(611))+t(429)+a))}else j(W(t(268))+t(347),1)}};var p=function(e,t,n){var r=_;if(e){if(e[r(563)])return e[r(563)];var i=s(e),a=e[r(473)]||e.$&&e.$[r(300)]}if(t)if(n||R(),r(284)==t)i=P[r(487)],a=P[r(538)];else i=P[r(640)],a=P[r(433)];return i&&a?r(615)+i+r(527)+a:""},f=function(e){var t=_;return t(287)===e||t(284)===e?{Rec_WvCid:p(null,e)}:{Rec_WvCid:e||"?"}},s=function(e){var t=_,n=v(e);return(n=n&&n[t(632)])&&n.id||0},v=function(e){var t=_,n=e[t(301)];return n&&n[t(447)]&&n[t(632)]?n:e[t(447)]&&e[t(632)]?e:void j(W(t(382)),1)},R=function(e){var t=_;if(!P[t(406)])return W(t(224));var n=p(null,1,1),r=P[t(354)][t(465)][t(250)];if(e){if(!P[t(487)])return W(t(369));if(p(null,t(284),1)!=n)return W(t(628))}return r&&r!=n&&j(W(t(486),0,r,n),3),""},m=P[_(579)]=function(e,t,n){var r=_,i=t&&t[r(307)];if(r(398)==i||3==e){var a=(t=t[r(308)])[r(502)],o=a[r(589)];900<o&&(a=a[r(500)](0,600)+r(608)+a[r(500)](o-300)),j(W(r(310),0,n)+"\n"+i+r(555)+a,3)}if(t&&i)return t;j(W(r(1==e?501:430),0,n),1)};function w(e,t){var n=y();return(w=function(e,t){return n[e-=192]})(e,t)}P[_(492)]=function(e,i,a){var c=_,t="";t||E||(t=W(c(315)));var o=!t&&function(e,t){var n=c;if(e&&e[n(563)])var r=/^wv_(\d+)_/[n(338)](e[n(563)]),i=r&&r[1];else{var a=e&&v(e),o=a&&a[n(447)];i=(a=a&&a[n(632)])&&a.id}if(i){if(i==P[n(640)])return P[n(406)];if(o)return o[n(196)]();var s=plus[n(257)][n(323)](i);if(s)return s}return t?(R(),P[n(406)]):null}(e,null==e);if(t||o||(t=W(c(null==e?443:192))),t)return t+=W(c(290)),j(t+c(514)+i[c(500)](0,200),1),t;var n=P[c(354)][c(465)];if(n[c(571)]||(n[c(571)]=1,r()),a){a instanceof ArrayBuffer||(j(c(410),1),a[c(509)]instanceof ArrayBuffer&&(a=a[c(509)]));var s=("a"+Math[c(471)]())[c(600)](".",""),u=0,l=function(){var e=c;if(0!=u&&u>=a[e(403)])o[e(195)](e(524)+s+e(256)+s+e(239)+i+e(357));else{var t=P[e(419)](l),n=u;u+=524288;var r=a[e(545)](n,u);o[e(195)](e(559)+s+e(253)+s+e(583)+a[e(403)]+e(221)+k(r)+e(208)+t+e(440))}};l()}else o[c(195)](c(417)+i+c(575))},P[_(207)]=function(e,t,n){var r=_,i="";r(413)==typeof t&&(i=t[r(609)]||"",t=t[r(348)]||"");var a="";a||E||(a=W(r(560)));var o=!a&&p(e,null==e);if(a||o||(a=W(r(null==e?446:565))),a)return a+=W(r(450)),j(a+r(514)+t[r(500)](0,200),1),a;P[r(492)](e,r(613)+i+r(512)+JSON[r(456)](W(r(511)))+r(294)+o+r(244)+JSON[r(456)](W(r(212)))+r(200)+JSON[r(456)](W(r(451)))+r(254)+t+r(458),n)},P[_(558)]=function(d,f,v,A){return new Promise(function(n,r){var i=w,a=(f=f||{})[i(535)]||"",o=-1==f[i(363)],t="",s=setTimeout(function(){var e=i;c(),s=0;var t=new Error(W(e(o?223:355),0,a));t[e(353)]=1,r(t)},o?2e3:f[i(363)]||5e3),c=function(){var e=P[i(354)];delete e[u],delete e[t]};o&&(t=P[i(419)](function(){clearTimeout(s)}));var e=function(e){var t=i;if(c(),s)return clearTimeout(s),s=0,e[t(334)]?n({value:e[t(528)],bigBytes:P[t(531)](e[t(334)])}):e[t(459)]?n(e[t(528)]):void r(new Error(a+e[t(490)]))},u=P[i(419)](e),l=i(539)+u+i(359)+u+i(312)+S+i(420)+u+i(544)+(o?i(392)+S+i(420)+t+i(337):"")+i(435)+JSON[i(456)](W(i(591)))+i(549)+JSON[i(456)](W(i(213),0,i(238)+V+'"'))+i(460);f[i(236)]?l+=v:l={preCode:l+=i(199),jsCode:v};var p=P[f[i(236)]?i(492):i(207)](d,l,A);p&&e({errMsg:p})})};var S=_(522),r=function(){var i=_;if(E&&i(383)!=typeof UniServiceJSBridge){var e=P[i(485)];if(e){var t="";try{t=uni[i(494)](S)}catch(e){}if(e==t)return;j(W(i(496)),3)}e="r"+Math[i(471)]();try{uni[i(255)](S,e)}catch(e){}P[i(485)]=e,UniServiceJSBridge[i(633)](S),UniServiceJSBridge[i(266)](S,function(e){var t=i,n=e[t(402)]||"";if(t(343)!=n)if(t(529)!=n)if(-1==n[t(525)](t(526)))-1==n[t(525)](t(210))?j(W(t(452))+JSON[t(456)](e),1):P[t(368)](e);else{var r=P[t(354)][n];r?r(e):j(W(t(336))+JSON[t(456)](e),3)}else J(e);else F(e)})}};P[_(419)]=function(t){var e=_,n=P[e(354)],r=++n.id,i=e(526)+r;return n[i]=function(e){delete n[i],t(e)},i},P[_(426)]=function(e,t){var n=_,r=P[n(354)],i=n(306)+e;return t?r[i]=t:delete r[i],i},P[_(508)]=function(e){UniViewJSBridge[_(311)](S,e)},P[_(219)]=function(r,i,e){var a=_;if(B&&N){var o=P[a(206)];if(o){r instanceof ArrayBuffer||(j(a(240),1),r[a(509)]instanceof ArrayBuffer&&(r=r[a(509)]));var s=P[a(354)],c=0,u=++s.id;s[a(210)+u]=function(e){c=e,t()};var l=0,t=function(){var e=a;if(0!=l&&l>=r[e(403)])return delete s[e(210)+u],void i(c);var t=l;l+=524288;var n=r[e(545)](t,l);P[e(508)]({action:e(t?220:358),wvCid:o,wvID:u,mainID:c,b64:k(n)})};t()}else e(W(a(324)))}else e(W(a(275)))},P[_(368)]=function(e){var t=_,n=e[t(577)],r=P[t(354)],i=t(210);t(358)==e[t(402)]&&(n=++r.id,r[i+n]={memory:new Uint8Array(2097152),mOffset:0});var a=r[i+n];if(a){var o=new Uint8Array(D(e[t(281)])),s=o[t(589)];if(a[t(444)]+s>a[t(350)][t(589)]){var c=new Uint8Array(a[t(350)][t(589)]+Math[t(623)](2097152,s));c[t(552)](a[t(350)][t(551)](0,a[t(444)])),a[t(350)]=c}a[t(350)][t(552)](o,a[t(444)]),a[t(444)]+=s,P[t(492)](f(e[t(399)]),t(506)+i+e[t(374)]+t(282)+n+t(372))}else j(W(t(639)),3)},P[_(531)]=function(e){var t=_;if(!E)return null;var n=P[t(354)],r=n[t(210)+e];return delete n[t(210)+e],r?r[t(350)][t(509)][t(545)](0,r[t(444)]):null},P[_(598)]=function(n,i,a,r){var o=_;a=a||I,r=r||I;var s=function(e){var t=w;r(W(t(243),0,n)+(e[t(267)]||e[t(490)]))};if(T){var e=wx[o(616)][o(448)]+"/"+n;wx[o(289)]()[o(233)]({filePath:e,encoding:o(585),data:i,success:function(){a(e)},fail:s})}else E?plus.io[o(572)](plus.io[o(597)],function(e){var t=o;e[t(247)][t(395)](n,{create:!0},function(n){var r=t;n[r(580)](function(e){var t=r;e[t(373)]=function(){a(n[t(518)])},e[t(217)]=s;try{e[t(596)](k(i))}catch(e){s(e)}},s)},s)},s):r(W(o(467)))};var i=function(e){var t=_;if(A(),E){var n=W(t(637),0,C),r=P[t(288)];r&&(!e&&P[t(588)]||(P[t(588)]=1,r[t(510)]?j(W(t(584))+n):j(W(t(384))+n))),P[t(316)]?r?!e&&P[t(554)]||(P[t(554)]=1,j(W(t(521))+n)):j(W(t(523))+n,1):P[t(610)]&&(P[t(406)]?!e&&P[t(541)]||(P[t(541)]=1,j(W(t(607))+n)):j(W(t(543))+a()+n,1))}},a=function(){return W(_(411))};P[_(252)]=function(e,t,n,a){var r=_,o=[],s=function(e){return W(w(385),0,e)};if(T){var c=function(n){var r=w;if(n>=t[r(589)])a[r(405)](e,o);else{var i=t[n];e[r(263)]()[r(397)](i)[r(469)]({node:!0})[r(338)](function(e){var t=r;e[0]?(o[t(211)](e[0][t(370)]),c(n+1)):j(s(i),1)})}};c(0)}else if(O){for(var i=2;i<=3;i++){var u=m(i,e[r(556)],r(304));if(!u)return;for(var l=0,p=t[r(589)];l<p;l++){var d=t[l],f=u[r(455)](d+r(231)),v=f[0],A=f[1];if(!v&&0==l&&2==i)break;if(i=9,!v)return void j(s(d),1);A&&(A[r(629)](r(259))||(v=f[1],A=f[0]),A[r(308)][r(193)](A)),v[r(479)][r(277)]=r(273),(A=document[r(520)](r(517)))[r(513)](r(259),"1"),A[r(479)][r(262)]=A[r(479)][r(497)]=r(568),v[r(308)][r(532)](A),o[r(211)](A)}}a[r(405)](e,o)}else{if(E){var R=[r(241)];for(l=0,p=t[r(589)];l<p;l++){d=t[l];R[r(211)](r(582)+d+r(586)+l+r(606)+JSON[r(456)](s(d))+r(567)+(l+1)+r(593))}return R[r(211)]("}"),R[r(211)](n),void P[r(207)](e,R[r(418)]("\n"))}j(W(r(258)),1)}};var U=function(){var r=_;g(r(248),{},null,null,function(e){var t=r,n=e[t(402)];t(592)==n?e[t(478)]?j("["+i+"]["+e[t(535)]+"]"+e[t(267)],1):j("["+i+"]["+e[t(535)]+"]"+e[t(267)]):t(540)==n?P[t(297)](e[t(595)],e[t(318)]):t(328)==n||j(W(t(407),0,i)+t(605)+n,3),P[t(230)]&&P[t(230)](e)});var e=P[r(288)],i=e&&e[r(510)]?l:r(590);e&&(P[r(627)]=1)},c=_(271),u=_(573)+c,l=c,A=P[_(489)]=function(){var e=_,t=P[e(288)],n="";if(!N)return"";if(!t)return P[e(235)]||W(e(428));if(B&&(n=W(e(503))),!n&&t[e(510)]){if(!P[e(576)]){for(var r=0,i=l=t[e(530)]||l,a=0;!r&&a<2;a++){try{r=uni[e(515)](i)}catch(e){}if(r||i!=c)break;j(W(e(439),0,c,i=c+"-"+(M?e(461):e(242))))}if(P[e(576)]=r)j(W(e(463),0,i));else{i=l==c?u:l;n=W(e(319),0,i)}}}else n||t[e(495)]||(n=W(e(533)));return n&&(P[e(288)]=null,j(n,1)),P[e(235)]=n},g=function(e,t,n,r,i){var a=_,o=A(),s=P[a(288)];if(s){var c={action:e,args:t||{}};i||(i=function(e){var t=a;t(445)==e[t(377)]?n&&n(e[t(528)],e):r&&r(e[t(267)])}),s[a(510)]?P[a(576)][a(495)](c,i):s[a(495)](c,i)}else r&&r(o)};P[_(422)]=function(r,i){return new Promise(function(t,n){var e=w;if(!E)return n(new Error(W(e(396))));P[e(627)]||U(),g(r,i,function(e){t(e)},function(e){n(new Error(e))})})},o[_(498)]=function(e,t){i(),e()},o[_(330)]=function(){return e(_(330))},o[_(237)]=function(){return e(_(237))};var e=function(e){var t=_;if(!d())return!1;var n=q[t(415)];if(n){var r=R(1);r?j(r,1):P[t(492)](f(n[t(480)]),t(468)+e+"()")}else j(W(t(566),0,e),3)};o[_(634)]=function(e,t,n){var s=_,r=q[s(415)];q[s(415)]=null,r&&d()&&P[s(492)](f(r[s(480)]),s(427)),!d()||P[s(298)]?(P[s(487)]=P[s(640)],P[s(538)]=P[s(433)],i(!0),function(r){var i=s;if(!E)return r();var e=P[i(386)]=P[i(386)]||{},n=function(e,t,n){j(W(i(376),0,V)+e,t||0),n||r()},t=P[i(288)];if(t||e[i(493)])return e[i(493)]=e[i(493)]||(t[i(510)]?2:1),2==e[i(493)]?n(W(i(534))):n(W(i(388)));var a=i(425)+(e[i(201)]=e[i(201)]||uni[i(499)]()[i(201)]||"0")+i(203);if(P[i(332)]){if(P[i(332)]==a)return n(a);j(W(i(292),0,a),3)}var o=function(e){var t=i;n(t(550)+V+t(604)+a+t(371)+u+t(340)+C+" ",3,e)};if(e[i(232)])return o();o(1),e[i(232)]=1,uni[i(542)]({title:i(548),content:"文件"+V+i(416),showCancel:!1,confirmText:i(564),complete:function(){r()}})}(function(){b(e,t,n)})):n(W(s(367)))};var b=function(i,a,v){var o=_;if(B)return P[o(202)]?void a():void v(W(o(409)));var A=function(){v(W(o(351)),!0)},e=function(n){var r=o;U(),j(W(r(483))),g(r(404),{},function(e){var t=r;1==e?(j(W(t(454))),n()):(j(W(t(331))+t(504)+e,1),A())},v)};if(P[o(316)])e(a);else{var s=f(o(284)),t=function(e){var n=o,t=R(1),r=W(n(507));t?v(r+t):P[n(558)](s,{tag:r,timeout:2e3,useEval:!0},n(378))[n(327)](function(){e()})[n(321)](function(e){var t=n;v(e[t(353)]?r+W(t(326)):e[t(267)])})},n=function(e){var n=o;if(P[n(333)](i)){var t=n(491),r=P[t]||{};P[n(558)](s,{timeout:-1},n(457)+!!e+n(280)+t+"="+JSON[n(456)](r)+n(360))[n(327)](function(e){var t=n;e.ok?a():v(e[t(490)],e[t(557)])})[n(321)](function(e){v(e[n(267)])})}else v(n(246))};P[o(288)]?t(function(){e(function(){n(!0)})}):t(function(){!function(p){var d=w;if(M){j(W(d(381)));var f=function(){var e=d;if(P[e(309)])p();else{var t=[],n=P[e(462)],r=e(362);if(!n){var i=plus[e(293)][e(228)](e(475));t[e(211)](i);var a=i[e(365)]();t[e(211)](a);var o=a[e(251)]();t[e(211)](o),n=P[e(462)]=o[e(204)]({objectForKey:r})}if(n){var s=plus[e(293)][e(228)](e(618))[e(466)](),c=s[e(404)]();1970168948==c?s[e(303)](f):1735552628==c?(j(W(e(578))+" "+r+":"+n),p()):(j(W(e(432))+e(342)+c,1),A()),t[e(211)](s)}else v(W(e(295),0,r));for(var u=0,l=t[e(589)];u<l;u++)plus[e(293)][e(631)](t[u])}};f()}else j(W(d(278))),plus[d(265)][d(400)]([d(361)],function(e){var t=d;0<e[t(601)][t(589)]?(j(W(t(302))+JSON[t(456)](e)),p()):(j(W(t(561)),1,e),A())},function(e){var t=d;j(W(t(622))+e[t(267)],1,e),v(W(t(216))+e[t(267)])})}(function(){n()})})}};function y(){var e=initMemory;return(y=function(){return e})()}o[_(484)]=function(t,o,n,s){var c=_,e=q[c(415)];if(q[c(415)]=null,e&&d()&&P[c(492)](f(e[c(480)]),c(427)),!d()||P[c(298)]){q[c(431)]=o;var u=h(o);if(u[c(552)][c(225)]=!0,u[c(434)]=c(344),q[c(553)]=!1,q[c(415)]=u,P[c(614)]=u,B)return P[c(202)]?void n():void s(W(c(215)));var r=function(t){var n=c,e=JSON[n(296)](JSON[n(456)](l));e[n(464)]=e[n(464)]||P[n(264)]||0,e[n(617)]=e[n(318)],e[n(318)]=48e3;var r=(e[n(205)]||{})[n(587)],i=e[n(314)];r&&null==i&&(i=1,e[n(314)]=!0),M||null!=e[n(546)]||(e[n(546)]=i?7:P[n(638)]||"0"),j(n(401)+JSON[n(456)](e)),U(),g(n(313),e,function(){var e=n;P[e(320)]=setInterval(function(){g(e(291),{},function(){})},5e3),t()},s)};clearInterval(P[c(320)]);var l={};for(var i in o)/_renderjs$/[c(624)](i)||(l[i]=o[i]);if(l=JSON[c(296)](JSON[c(456)](l)),P[c(316)])r(n);else{u[c(552)][c(519)]=c(453);var a=function(e,t){var n=c,r=R(1);if(r)s(W(n(423))+r);else{u[n(480)]=p(null,n(284)),q[n(553)]=t;var i=[n(612)+JSON[n(456)](l)+";"],a=n(621);i[n(211)](n(375)+(o[n(391)]||0)+n(626)+(o[n(570)]||0)+n(474)+(o[n(594)]||0)+n(537)+a+n(276)+a+n(270)),(o[n(339)]||o[n(449)])&&i[n(211)](n(345)+(o[n(449)]||0)+n(249)),i[n(211)](n(619)),P[n(558)](f(u[n(480)]),{timeout:-1},i[n(418)]("\n"))[n(327)](function(){e()})[n(321)](function(e){s(e[n(267)])})}};P[c(288)]?a(function(){var e=c;P[e(333)](t)?r(n):s(e(246))},!0):a(n)}}else s(W(c(305)))},o[_(603)]=function(e){return!!d()&&""},o[_(547)]=function(e){var t=_;if(!d())for(var n in e)/_renderjs$/[t(624)](n)&&delete e[n]};var F=function(e){var t=_,n=q[t(415)];n&&(n[t(552)][t(318)]=e[t(261)],n[t(552)][t(394)]=e[t(379)]);for(var r=e[t(630)],i=0,a=r[t(589)];i<a;i++)q(r[i],e[t(318)])},J=function(e){var t=_,n=q[t(415)];if(n){var r=new Uint8Array(D(e[t(436)]));n[t(552)][t(339)]&&n[t(552)][t(339)](r)}else j(W(t(488)),3)},q=function(e,t){var n=_,r=q[n(415)];if(r){if(r[n(470)]||r[n(197)]({envName:o[n(364)],canProcess:o[n(408)]()},t),r[n(470)]=1,e instanceof Int16Array)var i=new Int16Array(e);else i=new Int16Array(D(e));var a=x[n(245)](i);r[n(335)](i,a)}else j(W(n(272)),3)};P[_(297)]=function(e,t){var n=_;if(q[n(553)]){var r=q[n(415)];return r?void P[n(492)](f(r[n(480)]),n(285)+e+'",'+t+")"):void j(W(n(229)),3)}q(e,t)},o[_(636)]=function(n,i,r){var a=_,o=function(e){var t=w;P[t(333)](n)&&(q[t(415)]=null,s&&c&&d()&&P[t(492)](f(s[t(480)]),t(427))),r(e)},s=q[a(415)],c=!0,u=i?"":P[a(214)](),e=function(){var e=a;if(P[e(333)](n))if(q[e(415)]=null,s){if(j(e(269)+s[e(198)]+e(414)+s[e(218)]+e(481)+JSON[e(456)](q[e(431)])),!i)return l(),void o(u);s[e(380)](function(e,t,n){l(),i(e,t,n)},function(e){l(),o(e)})}else o(W(e(387))+(u?" ("+u+")":""));else o(e(246))},l=function(){var e=a;if(P[e(333)](n))for(var t in q[e(415)]=null,s[e(552)])q[e(431)][t]=s[e(552)][t]};if(B)return P[a(202)]?void e():void o(W(a(390)));var t=function(e){g(a(279),{},e,o)};if(clearInterval(P[a(320)]),P[a(316)])t(e);else{var p=function(e){var r=a;if(s){var t=R(1);if(t)o(W(r(322))+t);else{var n=r(569)+(i&&q[r(431)][r(349)]||0)+r(574)+!i+r(209);P[r(558)](f(s[r(480)]),{timeout:-1},n)[r(327)](function(e){var t=r;c=!1,s[t(552)][t(519)]=q[t(431)][t(519)],s[t(552)][t(318)]=e[t(261)],s[t(552)][t(394)]=e[t(379)],l();var n=P[t(531)](e[t(412)]);n?i(n,e[t(325)],e[t(562)]):o(W(t(389)))})[r(321)](function(e){c=!1,o(e[r(267)])})}}else o(W(r(477))+(u?" ("+u+")":""))};P[a(288)]?t(function(){var e=a;P[e(333)](n)?p():o(e(246))}):p()}}}();})(["UniWebViewEval bigBytes must be ArrayBuffer","e6Mo::，请检查此页面代码中是否编写了lang=renderjs的module，并且调用了RecordApp.UniRenderjsRegister；如果确实没有renderjs，比如nvue页面，请设置RecordApp.UniWithoutAppRenderjs=true并且搭配配套的原生插件在逻辑层中直接录音","dataId","object"," srcSR:","rec","在uni-app中编译到App平台时仅供测试用，不可用于正式发布或商用，正式发布或商用需先获得授权许可（编译到其他平台时无此授权限制，比如：H5、小程序，均为免费授权）。本对话框仅在第一次请求录音权限时会弹出一次，如何去除本弹框、如何获取商用授权、更多信息请看控制台日志","(function(){\n","join","UniMainCallBack",'",{action:"',"RegisterPlatform","UniNativeUtsPluginCallAsync","Bjx9::无法调用Start：","MiniProgramWx_onShow","我已获得UniAppID=","UniMainCallBack_Register","RecordApp.Stop()","H753::未配置RecordApp.UniNativeUtsPlugin原生录音插件"," WvCid=","dX6B::{1}需要传入当前页面或组件的this对象作为参数","param","iKhe::plus.ios请求录音权限，状态值: ","__uniAppComponentId","dataType","\n\t\tif(!window.RecordApp){\n\t\t\treturn CallFail(","bytes","miniProgram-wx","4ATo::Recorder-UniCore目前只支持：H5、APP(Android iOS)、MP-WEIXIN，其他平台环境需要自行编写适配文件实现接入","kSjQ::当前App未打包进双端原生插件[{1}]，尝试加载单端[{2}]",'"});\n\t\t})()',"$ownerInstance","RecordApp.UniRenderjsRegister","peIm::当前还未调用过RecordApp.UniWebViewActivate","mOffset","success","mSbR::当前还未调用过RecordApp.UniWebViewActivate","$scope","USER_DATA_PATH","takeoffEncodeChunk_renderjs","TtoS::，不可以调用RecordApp.UniWebViewVueCall","URyD::没有找到组件的renderjs模块","ZHwv::[MainReceive]从renderjs发回未知数据：","unknown","Lx6r::已获得录音权限","querySelectorAll","stringify","\n\t\t\tRecordApp.UniAppUseNative=","\n\t\t}).call(vm);\n\t})()","isOk",");\n\t\t};\n\t","iOS","__9xoE","Xh1W::已加载原生录音插件[{1}]","appNativePlugin_sampleRate","pageShow","sharedInstance","kxOd::当前环境未支持保存本地文件","RecordApp.","fields","_appStart","random","__rModule","_$id",";\n\t\tvar startFn=","NSBundle","1356906lijWyv","pP4O::未开始录音","isError","style","__wvCid"," set:","$vm","Lx5r::正在调用原生插件请求录音权限","Start","__uniAppMainReceiveBind","SWsy::检测到有其他页面或组件调用了RecordApp.UniPageOnShow（WvCid={1}），但未调用过RecordApp.UniWebViewActivate（当前WvCid={2}），部分功能会继续使用之前Activate的WebView和组件，请确保这是符合你的业务逻辑，不是因为忘记了调用UniWebViewActivate","__uniAppReqWebViewId","MTdp::未开始录音，但收到renderjs回传的onRecEncodeChunk","UniCheckNativeUtsPluginConfig","errMsg","RequestPermission_H5OpenSet","UniWebViewEval","uts","getStorageSync","request","vEgr::不应该出现的MainReceiveBind重复绑定","height","Install","getSystemInfoSync","substr","dX5B::{1}需在renderjs中调用并且传入当前模块的this","outerHTML","l6sY::renderjs中不支持设置RecordApp.UniNativeUtsPlugin"," code=","ownerId",'(function(){\n\t\tvar fn=RecordApp.__UniData["',"ksoA::无法调用RequestPermission：","UniWebViewSendToMain","buffer","nativePlugin","U1Be::renderjs中未import导入RecordApp","\n\t\tif(!window.RecordApp){\n\t\t\tvar err=","setAttribute","   jsCode=","requireNativePlugin","rec_wv_cid_key","canvas","fullPath","type","createElement","xYRb::当前RecordApp运行在逻辑层中（性能会略低一些，可视化等插件不可用）","RecordApp__uniAppMainReceive","fqhr::当前已配置RecordApp.UniWithoutAppRenderjs，必须提供原生录音插件或uts插件才能录音，请参考RecordApp.UniNativeUtsPlugin配置","(function(){\n\t\t\t\tvar BigBytes=window.","indexOf","mainCb_","_cid_","value","recEncodeChunk","nativePluginName","UniMainTakeBigBytes","appendChild","TGMm::提供的RecordApp.UniNativeUtsPlugin值不是RecordApp的uts原生录音插件","w37G::已购买原生录音插件，获得授权许可","tag","897508WgBLZb",";\n\t\tset.onProcess=function(","__uniAppReqComponentId",'\n\t\tvar CallSuccess=function(val,buf){\n\t\t\tif(buf){\n\t\t\t\tRecordApp.UniWebViewSendBigBytesToMain(buf,function(dataID){\n\t\t\t\t\tRecordApp.UniWebViewSendToMain({action:"',"onRecord","__0hyi","showModal","S3eF::未找到当前页面renderjs所在的WebView",'",errMsg:err});\n\t\t};',"slice","android_audioSource","AllStart_Clean","未获得商用授权时，App上仅供测试哈",');\n\t\t};\n\t\tif(!RecordApp.Platforms["UniApp-Renderjs"]){\n\t\t\treturn CallFail(',"当前未获得授权许可。文件","subarray","set","nativeToRjs","__xYRb"," parentNode:\n","$el","isUserNotAllow","UniWebViewCallAsync","(function(){\n\t\t\tvar cur=window.","lU1W::当前不是App逻辑层","Ruxl::plus.android请求录音权限：无权限","mime","Rec_WvCid","我知道啦","6Iql::未找到此页面renderjs所在的WebView Cid","0FGq::未开始录音，不可以调用{1}",',1);\n\t\t\t\t\treturn;\n\t\t\t\t}else{\n\t\t\t\t\tif(el2){\n\t\t\t\t\t\tif(!el2.getAttribute("el2")){ el=els[1]; el2=els[0] }\n\t\t\t\t\t\tel2.parentNode.removeChild(el2);\n\t\t\t\t\t}\n\t\t\t\t\tel.style.display="none";\n\t\t\t\t\tel2=document.createElement("canvas");\n\t\t\t\t\tel2.setAttribute("el2","1"); el2.style.width=el2.style.height="100%";\n\t\t\t\t\tel.parentNode.appendChild(el2);\n\t\t\t\t}\n\t\t\t\tvar canvas',"100%","(function(){\n\t\t\tvar stopFn=","onProcessBefore_renderjs","mrBind","requestFileSystem","https://ext.dcloud.net.cn/plugin?name=",";\n\t\t\tvar clear=","\n})()","__uniNP","mainID","j15C::已获得iOS原生录音权限","__dX7B","createWriter","UniPageOnShow",'\n\t\t\t\tvar els=cpEl.querySelectorAll("',"||{memory:new Uint8Array(","XSYY::当前录音由原生录音插件提供支持","binary",' canvas"),el=els[0],el2=els[1];\n\t\t\t\tif(!el && ',"echoCancellation","__nnM6","length","RecorderUtsPlugin","TSmQ::需要在页面中提供一个renderjs，在里面import导入RecordApp、录音格式编码器、可视化插件等","onLog","=el2;\n\t\t\t","start_renderjs","pcmDataBase64","writeAsBinary","PUBLIC_DOWNLOADS","UniSaveLocalFile","Platforms","replace","granted","UniWebViewActivate","Start_Check","在uni-app中编译到App平台时仅供测试用（App平台包括：Android App、iOS App），不可用于正式发布或商用，正式发布或商用需先获取到商用授权许可（编译到其他平台时无此授权限制，比如：H5、小程序，均为免费授权）。未获得授权时，在App打开后第一次调用RecordApp.RequestPermission请求录音权限时，会先弹出商用授权提示框；获取到授权许可后，请在调用RequestPermission前设置 RecordApp.UniAppUseLicense='","action=","==0 && type==2) continue; type=9; //尝试获取el的上级来查询\n\t\t\t\tif(!el){\n\t\t\t\t\tRecordApp.CLog(","0hyi::当前RecordApp运行在renderjs所在的WebView中（逻辑层中只能做有限的实时处理，可视化等插件均需要在renderjs中进行调用）","\n...\n","preCode","__hasWvActivate","7kJS::RecordApp.UniRenderjsRegister 已注册当前页面renderjs模块","var set=","(function(){\n\t\tvar CallErr=function(){};\n\t\t","__Rec","wv_","env","sampleRate_set","AVAudioSession","RecordApp.Start(set,function(){\n\t\t\tstartFn&&startFn.call(This);\n\t\t\tCallSuccess();\n\t\t},function(errMsg){\n\t\t\tCallFail(errMsg);\n\t\t});","4405QUTQBY","buffers,power,duration,sampleRate,newIdx","0JQw::plus.android请求录音权限出错：","max","test","mzKj::RecordApp.UniRenderjsRegister 重复注册当前页面renderjs模块，一个组件内只允许一个renderjs模块进行注册",";\r\n\t\tvar procBefore=","__uniNbjc","VsdN::需重新调用RecordApp.RequestPermission方法","getAttribute","newBuffers","deleteObject","$page","unsubscribe","RequestPermission","RecApp Main","Stop","1f2V:: | RecordApp的uni-app支持文档和示例：{1} ","Default_Android_AudioSource","CjMb::无效的BigBytes回传数据","__uniAppWebViewId","qDo1::未找到此页面renderjs所在的WebView","removeChild","RXs7::微信小程序中需要：{1}","evalJS","$getAppWebview","envStart","recSize","CallErr=function(err){ CallFail(err) };",';\n\t\t\t\tRecordApp.CLog(err,1); CallErr(err); return;\n\t\t\t};\n\t\t\tvar el=document.querySelector("[rec_wv_cid_key=\'"+wvCid+"\']");\n\t\t\tvm=el&&el.__rModule;\n\t\t\tif(!vm){\n\t\t\t\tvar err=',"appId","UniAppUseNative","的商用授权","plusCallMethod","audioTrackSet","__UniWvCid","UniWebViewVueCall",'"));\n\t\t\tcur.memory.set(buf,cur.mOffset);\n\t\t\tcur.mOffset+=buf.byteLength;\n\t\t\tRecordApp.UniWebViewSendToMain({action:"',';\n\t\t\tvar errFn=function(errMsg){\n\t\t\t\tCallFail(errMsg);\n\t\t\t};\n\t\t\tRecordApp.Stop(clear?null:function(arrBuf,duration,mime){\n\t\t\t\tstopFn&&stopFn.apply(This,arguments);\n\t\t\t\tvar recSet=RecordApp.__Rec.set,t1=Date.now();\n\t\t\t\tRecordApp.CLog("开始传输"+arrBuf.byteLength+"字节的数据回逻辑层，可能会比较慢，推荐使用takeoffEncodeChunk实时获取音频文件数据可避免Stop时产生超大数据回传");\n\t\t\t\tRecordApp.UniWebViewSendBigBytesToMain(arrBuf,function(dataId){//数据可能很大\n\t\t\t\t\tRecordApp.CLog("完成传输"+arrBuf.byteLength+"字节的数据回逻辑层，耗时"+(Date.now()-t1)+"ms");\n\t\t\t\t\tCallSuccess({recSet_sr:recSet.sampleRate,recSet_bit:recSet.bitRate,dataId:dataId,duration:duration,mime:mime});\n\t\t\t\t},errFn);\n\t\t\t},errFn);\n\t\t})()',"bigBytes_","push","Bcgi::renderjs中的mounted内需要调用RecordApp.UniRenderjsRegister","AN0e::需在renderjs中import {1}","__StopOnlyClearMsg","rSLO::不应当出现的非H5录音Start","Mvl7::调用plus的权限请求出错：","onerror","srcSampleRate","UniWebViewSendBigBytesToMain","bigBytes_chunk",'),mOffset:0};\n\t\t\tvar buf=new Uint8Array(RecordApp.UniAtob("',"WpKg::RecordApp.UniWebViewActivate 已切换当前页面或组件的renderjs所在的WebView","KQhJ::{1}连接renderjs超时","AGd7::需要先调用RecordApp.UniWebViewActivate方法","disableEnvInFix","UniApp-Renderjs","__WebVieW_Id__","importClass","byzO::未开始录音，但收到UniNativeUtsPlugin PCM数据","UniNativeUtsPlugin_JsCall"," canvas","show","writeFile","GwCz::RecordApp.UniWebViewActivate 需要传入当前页面或组件的this对象作为参数","__uniNupErr","useEval","Resume",'"@/uni_modules/Recorder-UniCore/',";\n\t\t\t\t","UniWebViewSendBigBytesToMain buffer must be ArrayBuffer",'\n\t\t\tfor(var type=2;type<=3;type++){//@@Fast\n\t\t\t\tvar cpEl=RecordApp.__dX7B(type, this.$ownerInstance.$el, "RecordApp.UniFindCanvas.renderjs");\n\t\t\t\tif(!cpEl) return; //wvFixEl()\n\t\t',"Android","UqfI::保存文件{1}失败：",'",vm=RecordApp.__uniWvCallVm;\n\t\tif(!vm || RecordApp.__uniWvCallWvCid!=wvCid){\n\t\t\tif(!RecordApp.__UniData[wvCid]){\n\t\t\t\tvar err=',"pcm_sum","Incorrect sync status","root","jsCall",';\n\t\t\tset.takeoffEncodeChunk=function(bytes){\n\t\t\t\tRecordApp.UniWebViewSendToMain({action:"recEncodeChunk",bytes:RecordApp.UniBtoa(bytes.buffer)});\n\t\t\t\ttakeFn&&takeFn.apply(This,arguments);\n\t\t\t};',"sWvCid","infoDictionary","UniFindCanvas","=window.",'+" WvCid="+wvCid;\n\t\t\t\tRecordApp.CLog(err,1); CallErr(err); return;\n\t\t\t};\n\t\t\tRecordApp.__uniWvCallVm=vm;\n\t\t\tRecordApp.__uniWvCallWvCid=wvCid;\n\t\t}; (function(){ var This=this;\n\t\t\t',"setStorageSync",".memory.buffer; delete window.","webview","yI24::RecordApp.UniFindCanvas未适配当前环境","el2"," WvCid=wv_","recSet_sr","width","createSelectorQuery","Default_AppNativePlugin_SampleRate","android","subscribe","message","Uc9E::RecordApp.UniRenderjsRegister 发生不应该出现的错误（可能需要升级插件代码）：","rec encode: pcm:",');\n\t\t\tvar newBuffers=[],recSet=RecordApp.__Rec.set;\n\t\t\tfor(var i=newIdx;i<buffers.length;i++)newBuffers.push(RecordApp.UniBtoa(buffers[i].buffer));//@@Fast\n\t\t\tRecordApp.UniWebViewSendToMain({action:"recProcess",recSet_sr:recSet.sampleRate,recSet_bit:recSet.bitRate,sampleRate:sampleRate,newBuffers:newBuffers});\n\t\t\treturn procFn&&procFn.apply(This,arguments);\n\t\t};',"Recorder-NativePlugin","BjGP::未开始录音，但收到Uni Native PCM数据","none","892185CAKnjA","MujG::只允许在renderjs中调用RecordApp.UniWebViewSendBigBytesToMain","){\r\n\t\t\tprocBefore&&procBefore.call(This,","display","7Noe::正在调用plus.android.requestPermissions请求Android原生录音权限","recordStop",";\r\n\t\t\tRecordApp.Current=null; //需先重置，不然native变化后install不一致\n\t\t\tRecordApp.","b64",'"];\n\t\tif(fn)fn(',"4012128ZFFyqy","@req",'RecordApp.UniNativeRecordReceivePCM("',"UniApp-Main","@act","UniNativeUtsPlugin","getFileSystemManager","igw2::，不可以调用RecordApp.UniWebViewEval","recordAlive","aPoj::UniAppUseLicense填写无效，如果已获取到了商用授权，请填写：{1}，否则请使用空字符串","ios",';\n\t\t\twindow["console"].error(err); CallErr(err); return;\n\t\t};\n\t\tvar wvCid="',"9xoE::项目配置中未声明iOS录音权限{1}","parse","UniNativeRecordReceivePCM","__callWvActivate","UniRenderjsRegister","uid","$root","Bgls::已获得Android原生录音权限：","requestRecordPermission","RecordApp.UniFindCanvas.H5","XCMU::需先调用RecordApp.UniWebViewActivate，然后才可以调用Start","mainCb_reg_","nodeName","parentNode","DisableIOSPlusReqPermission","dX7B::{1}未正确查询到节点，将使用传入的当前页面或组件this的$el.parentNode作为组件根节点。如果template下存在多个根节点(vue3 multi-root)，尽量在最外面再套一层view来避免兼容性问题","publishHandler",'", isOk:true, value:val});\n\t\t\t}\n\t\t};\n\t\tvar CallFail=function(err){\n\t\t\tUniViewJSBridge.publishHandler("',"recordStart","appNativePlugin_AEC_Enable","TfJX::当前不是App逻辑层","UniWithoutAppRenderjs","1117214uNlouf","sampleRate","SCW9::配置了RecordApp.UniNativeUtsPlugin，但当前App未打包进原生录音插件[{1}]","_X3Ij_alive","catch","H6cq::无法调用Stop：","getWebviewById","kE91::renderjs中的mounted内需要调用RecordApp.UniRenderjsRegister才能调用RecordApp.UniWebViewSendBigBytesToMain","duration","KnF0::无法连接到renderjs","then","noop","Tag","Pause","Lx7r::无录音权限","UniAppUseLicense","__Sync","dataID","envIn","kZx6::从renderjs发回数据但UniMainCallBack回调不存在：",'"});\n\t\t',"exec","takeoffEncodeChunk","-Android （会赠送Android版原生插件）；购买后可联系客服，同时提供订单信息，客服拉你进入VIP支持QQ群，入群后在群文件中可下载此js文件最新源码；客服联系方式：QQ 1251654593 ，或者直接联系作者QQ 753610399 （回复可能没有客服及时）。详细请参考文档: ","RecApp Renderjs","denied ","recProcess","arraybuffer","var takeFn="," wvCid=","!id || !cid","jsCode","stop_renderjs","memory","0caE::用户拒绝了录音权限","503716XEwjom","isTimeout","__UniData","RDcZ::{1}处理超时","3jVZBMI","\n\t\t\t})()","bigBytes_start",'", isOk:true, value:val, dataID:dataID});\n\t\t\t\t},CallFail)\n\t\t\t}else{\n\t\t\t\tRecordApp.UniWebViewSendToMain({action:"',";\n\t\t\tRecordApp.RequestPermission(function(){\n\t\t\t\tCallSuccess({ok:1});\n\t\t\t},function(errMsg,isUserNotAllow){\n\t\t\t\tCallSuccess({errMsg:errMsg,isUserNotAllow:isUserNotAllow});\n\t\t\t});\n\t\t","android.permission.RECORD_AUDIO","NSMicrophoneUsageDescription","timeout","Key","mainBundle","import 'recorder-core/src/app-support/app-miniProgram-wx-support.js'","PkQ2::需先调用RecordApp.UniWebViewActivate，然后才可以调用RequestPermission","__UniMainReceiveBigBytes","7ot0::需先调用RecordApp.RequestPermission方法","node","' ，就不会弹提示框了；或者购买了配套的原生录音插件，设置RecordApp.UniNativeUtsPlugin参数后，也不会弹提示框。【获取授权方式】到DCloud插件市场购买授权: ",");\n\t})()","onwrite","wvID","var procFn=","FabE::【在App内使用{1}的授权许可】","status","CallSuccess(1)","recSet_bit","stop","Y3rC::正在调用plus.ios@AVAudioSession请求iOS原生录音权限","KpY6::严重兼容性问题：无法获取页面或组件this.$root.$scope或.$page","undefined","nnM6::当前录音由uts插件提供支持","k7im::未找到Canvas：{1}，请确保此DOM已挂载（可尝试用$nextTick等待DOM更新）","__FabE","YP4V::未开始录音","e71S::已购买uts插件，获得授权许可","gomD::不应该出现的renderjs发回的文件数据丢失","TPhg::不应当出现的非H5录音Stop","onProcess_renderjs",'\n\t\t\tUniViewJSBridge.publishHandler("',"ipB3::RecordApp.UniWebViewActivate 发生不应该出现的错误（可能需要升级插件代码）：","bitRate","getFile","MrBx::需在App逻辑层中调用原生插件功能","select","#text","wvCid","requestPermissions","Native Start Set:","action","byteLength","recordPermission","apply","__uniAppWebView","dl4f::{1}回传了未知内容，","CanProcess","Jk72::不应当出现的非H5权限请求"])}(i,r,0,a.$T,t)}else console.error("需要先引入RecordApp，请按下面代码引入：\n1. 项目根目录 npm install recorder-core\n2. 页面中按顺序import\nimport Recorder from 'recorder-core'\nimport RecordApp from 'recorder-core/src/app-support/app.js'\nimport 你需要的音频格式编码器、可视化插件\n参考文档："+i)}();