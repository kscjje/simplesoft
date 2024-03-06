

;(function ($, win, doc, undefined) {
	
	var namespace = 'bsnbc';

	$.fn.inView = function() {
		var $el = $(this);
		var _flag = false;

		var _top = $el.offset().top;
		var _left = $el.offset().left;
		var _height = $el.outerHeight();
		var _width = $el.outerWidth();

		_flag = _top < (window.pageYOffset + window.innerHeight) &&
				_left < (window.pageXOffset + window.innerWidth) &&
				(_top + _height) > window.pageYOffset &&
				(_left + _width) > window.pageXOffset;

		return _flag;
	};

	win[namespace] = {
		status: {
			scrollY: 0,
			scrollDirection: '',
			scrollIsHome: function(){
				return win[namespace].status.scrollY === 0 ? true : false;
			},
			scrollIsEnd: function(){
				return win[namespace].status.scrollY + $(win).outerHeight() === $(doc).outerHeight() ? true : false;
			},
			scrollCheck: {
				beforeScrollY: 0,
				direction: function(){
					return win[namespace].status.scrollCheck.beforeScrollY < win[namespace].status.scrollY ? 
						'down' : 'up';
				},
				init: function(){
					function bodyAddClass() {
						var $body = $('body');
						if (!!win[namespace].status.scrollIsHome()) {
							$body.addClass('is-home');
						} else if (!!win[namespace].status.scrollIsEnd()) {
							$body.addClass('is-end');
						} else {
							$body.removeClass('is-home is-end');
						}
					}
					win[namespace].status.scrollY = $('html').prop('scrollTop');
					win[namespace].status.scrollIsHome();
					win[namespace].status.scrollIsEnd();
					bodyAddClass();

					$(doc).off('scroll.scrollCheck').on('scroll.scrollCheck', function(){
						win[namespace].status.scrollY = $('html').prop('scrollTop');
						win[namespace].status.scrollDirection = win[namespace].status.scrollCheck.direction();
						win[namespace].status.scrollCheck.beforeScrollY = win[namespace].status.scrollY;
						win[namespace].status.scrollIsHome();
						win[namespace].status.scrollIsEnd();
						bodyAddClass();
					});

				}
			}
		},
		navLoad: function(){
			(function () {
				return new Promise(function(resolve, reject) {
					$.get('../include/header.html', function(response) {
						if (response) {
							resolve(response);
						}
						reject(new Error('Request is failed'));
					});
				});
			})()
			.then(function(data) {
				$('.header-area').html(data);
				// win[namespace].nav.hoverMenu(); // hover evt on nav
				win[namespace].nav.slidingMenu(); // show/hide evt on nav
				win[namespace].nav.openDepth2(); // 2depth links evt on nav
				
			}).catch(function(err) {
				console.error('win.'+namespace+'.navLoad failed!!');
			});
		},
		footerLoad: function(){
			(function () {
				return new Promise(function(resolve, reject) {
					$.get('../include/footer.html', function(response) {
						if (response) {
							resolve(response);
						}
						reject(new Error('Request is failed'));
					});
				});
			})().then(function(data) {
				$('.footer-area').html(data);
			}).catch(function(err) {
				console.error('win.'+namespace+'.footerLoad failed!!');
			});
		},
		nav: {
			hoverMenu: function(){
				var $header = $('.header-area');
				var $links = $header.find('.gnb-area a');
				var flag = {};

				$links
          .off('.openMenuPC')
          .on('mouseenter.openMenuPC focus.openMenuPC', function () {
						$header.addClass('hover');
						clearTimeout(flag);
            $header
              .off('.closeMenuPC')
              .on('mouseleave.closeMenuPC', removeHover);
            $links.off('.closeMenuPC').on('blur.closeMenuPC', removeHover);
          });
					
				function removeHover() {
					flag = setTimeout(function () {
            $header.removeClass('hover');
          }, 1);
        }
			},
			slidingMenu: function(){
				var $header = $('.header-area');
				var $btnMenu = $header.find('.btn-menu');
				
				$btnMenu
					.off('.openMenu')
					.on('click.openMenu', function(){
						$header.toggleClass('open');

						//2022.12.19 추가
						if($header.is(".open") === true){
							if($("body").find(".header-bar").length){
								if($(".header-bar").is(":visible")){
									$("#wrap").removeClass("barOn");
									$(".header-bar").slideUp();
								}
							}
						}else{
							if($("body").find(".header-bar").length){
								if($(".header-bar").is(".off") === false){
									$("#wrap").addClass("barOn");
									$(".header-bar").slideDown();
								}
							}
						}
				});
			},
			openDepth2: function(){
				var $header = $('.header-area');
				var $listDepth1 = $header.find('.nav-d1 > li');
				var $btnDepth1 = $listDepth1.children('a');

			//	console.log($btnDepth1);
				$btnDepth1.on('click', function(e){
					if ($(win).outerWidth() < 1025 && !!$(this).siblings('.nav-d2').length) {
						var $parentList = $(this).closest('li');
						e.preventDefault();
						$listDepth1.not($parentList).removeClass('on');
						$parentList.toggleClass('on');
					}
				});
			}
		},
		mainSlider: {
			slide: {},
			init: function(){
				win[namespace].mainSlider.slide = $('.slider-visual .slider-inner').slick({
					infinite: true,
					speed: 400,
					autoplay: true,
					autoplaySpeed: 5000,
					arrows: true,
					pauseOnHover: false,
					dots: true,
					appendDots: '.slider-pagination',
					customPaging : function(slider, idx) {
							return '<a href="#">'+ ((idx < 9)?'0'+ ++idx : ++idx) +'</a>';
					},
				})

				/*
				$('.btn-rolling').on('click', function(){
					if ($(this).hasClass('play')) {
						$(this).removeClass('play');
						$(this).addClass('pause');
						$('.slider-visual .slider-inner').slick('slickPlay');
					} else {
						$(this).removeClass('pause');
						$(this).addClass('play');
						$('.slider-visual .slider-inner').slick('slickPause');
					}
				})
				*/
			}
		},
		skillAni:{
			timer: {},
			init: function(isShow){
				var _idx = 0;
				var $item = $('.skill-listNEW li');

				clearInterval(win[namespace].skillAni.timer);
				if (win[namespace].checkBrowserSize() === 'mobile') { 
					$item.removeClass('active');
					return false;
				 }
				win[namespace].skillAni.timer = setInterval(function(){
					$item.removeClass('active');
					$item.eq(_idx).addClass('active');
					if (_idx > 6) {
						_idx = 0;
					} else {
						_idx++;
					}
				}, 2000)
			}
		},
		//2022.12.21 추가
		registerAni:{
			timer: {},
			init: function(isShow){
				var _idx = 0;
				var $item = $('.register-step--list li');

				clearInterval(win[namespace].registerAni.timer);
				if (win[namespace].checkBrowserSize() === 'mobile') { 
					$item.removeClass('active');
					return false;
				 }
				win[namespace].registerAni.timer = setInterval(function(){
					$item.removeClass('active');
					$item.eq(_idx).addClass('active');
					if (_idx > 5) {
						_idx = 0;
					} else {
						_idx++;
					}
				}, 1500)
			}
		},
		guideVod: function() {
			var $item = $('.guide-list .item');

			$item.on('click', function(){
				var _prev = $(this).attr('data-status');
				var $now = $item.filter('[data-status="active"]');

				if (win[namespace].checkBrowserSize() !== 'mobile') {
					if (_prev !== 'active'){
						$now.attr('data-status', _prev);
						$(this).attr('data-status', 'active');
						videojs($now.find('.video-js').attr('id')).pause();
					}
				} else {
					videojs($item.not($(this)).find('.video-js').eq(0).attr('id')).pause();
					videojs($item.not($(this)).find('.video-js').eq(1).attr('id')).pause();
				}
			})
		},
		isBrowser: function(){
			var agt = navigator.userAgent.toLowerCase(); 
			if (agt.indexOf("chrome") != -1) return 'Chrome'; 
			if (agt.indexOf("opera") != -1) return 'Opera'; 
			if (agt.indexOf("staroffice") != -1) return 'Star Office'; 
			if (agt.indexOf("webtv") != -1) return 'WebTV'; 
			if (agt.indexOf("beonex") != -1) return 'Beonex'; 
			if (agt.indexOf("chimera") != -1) return 'Chimera'; 
			if (agt.indexOf("netpositive") != -1) return 'NetPositive'; 
			if (agt.indexOf("phoenix") != -1) return 'Phoenix'; 
			if (agt.indexOf("firefox") != -1) return 'Firefox'; 
			if (agt.indexOf("safari") != -1) return 'Safari'; 
			if (agt.indexOf("skipstone") != -1) return 'SkipStone'; 
			if (agt.indexOf("netscape") != -1) return 'Netscape'; 
			if (agt.indexOf("mozilla/5.0") != -1) return 'Mozilla'; 
			if (agt.indexOf("msie") != -1) { 
					var rv = -1; 
				if (navigator.appName == 'Microsoft Internet Explorer') { 
					var ua = navigator.userAgent; var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})"); 
				if (re.exec(ua) != null) 
					rv = parseFloat(RegExp.$1); 
				} 
				return 'IE '+rv; 
			} 
		},
		cookieControl: {
			setCookie: function ( name, value, expiredays ) {
				var todayDate = new Date();
				todayDate.setDate( todayDate.getDate() + expiredays );
				document.cookie = name + '=' + escape( value ) + '; path=/; expires=' + todayDate.toGMTString() + ';'
				console.log(document.cookie);
			},
			isHasCookie: function () {
				var cookiedata = document.cookie;
				console.log(cookiedata);
				if ( cookiedata.indexOf('todayCookie=done') < 0 ){
						return false;
				}
				else {
						return true;
				}
			}
		},
		scrollToTop: function(){
			$('.floating-util .top').on('click', function(){
				$('body, html').animate({
					scrollTop: 0
				}, 200)
			})
		},
		checkBrowserSize: function(){
			var _winW = $(win).outerWidth();
			var size = '';
			
			if (_winW < 764) {
				size = 'mobile';
			} else if (_winW < 1025) {
				size = 'tablet';
			} else {
				size = 'pc';
			}
			$('html').attr('data-size', size);

			return size;
		},
		isInview: function($el, callback){
			$.extend({}, {
				inBack: function() {},
				outBack: function() {}
			}, callback)
			$(doc).on('scroll.'+namespace, function(){
				if ($el.inView()) {
					$el.addClass('ui-in');
				} else {
					$el.removeClass('ui-in');
				}
			});
		},
		stepRolling: {
			flag: false,
			init: function(){
				var $wrap = $('.register-area');
				if (!$wrap.hasClass('ui-in')) { 
					$wrap.stop().scrollLeft(0);
					win[namespace].stepRolling.flag = false;
				} else if (!win[namespace].stepRolling.flag) {
					win[namespace].stepRolling.flag = true;
					$wrap.animate({
						scrollLeft: 600
					}, 12000, 'linear');

					$wrap.on('touchstart', function(){
						$wrap.stop();
					})
				}
			}
		},
		accoInfo: function(){
			$('.btn-acco').off('click.accoInfo').on('click.accoInfo', function(){
				var $ts = $(this);
				var $target = $('#'+$ts.attr('data-pnl'));
				if ($ts.hasClass('open')) { 
					$ts.removeClass('open');
					$target.stop().slideUp(200);
				} else {
					$ts.addClass('open');
					$target.stop().slideDown(200);
				}
			})
		},
		tab: {
			init: function(){
				$('.ui-tab').each(function(idx, item){
					var $wrap = $(item);
					var $btns = $wrap.find('.ui-tab-btn');
					var $pnl = $wrap.find('.ui-tab-pnl');
					var $nowBtn = $btns.filter('.selected');
					var _idx = $btns.index($nowBtn);
					
					console.log($('.ui-tab'));
					$pnl.eq(_idx).show();
				})
				$('.ui-tab-btn').on('click', function(){
					var $ts = $(this);
					var $wrap = $ts.closest('.ui-tab');
					var $btns = $wrap.find('.ui-tab-btn');
					var idx = $btns.index($ts);
					var $pnls = $wrap.find('.ui-tab-pnl');
					
					$btns.removeClass('selected');
					$ts.addClass('selected');
					$pnls.hide();
					$pnls.eq(idx).show();
				})
			}
		},
		modal:{
			open: function(targetId){
				if (!$('.layer-wrap').length) {
					$('main').append($('<div class="layer-wrap"></div>'));
				}
				
				var $bg = $('.layer-wrap');
				var $target = $('#' + targetId);

				$bg.show();
				$target.appendTo($bg).show().addClass('open');
			},
			close: function(targetId) {
				var $bg = $('.layer-wrap');
				var $target = $('#' + targetId);
				var _isVisible = false;

				$target.hide().removeClass('open');
				$bg.find('.ui-modal').each(function(idx, item){
					_isVisible = !!$(item).is(':visible') ? true : _isVisible;
				})
				if (!_isVisible){
					$bg.hide();
				}
			}
		},
		init: function(){

			$(win).off('.'+namespace)
				.on('resize.'+namespace, function(){
					win[namespace].checkBrowserSize();
					win[namespace].skillAni.init(true);
					win[namespace].registerAni.init(true);

					//2022.12.19 추가
					if ($(win).outerWidth() > 1025){
						if($(".header-bar").is(".off") === false){
							$("#wrap").addClass("barOn");
							$(".header-bar").show();
						}
						$(".header-area").removeClass("open");
					}	
				});

			$(doc).on('ready.'+namespace, function(){
				win[namespace].checkBrowserSize();
				$('html').addClass(win[namespace].isBrowser());
			//	win[namespace].navLoad();
			//	win[namespace].footerLoad();
				win[namespace].status.scrollCheck.init();
				win[namespace].guideVod();
				win[namespace].scrollToTop();
				win[namespace].accoInfo();
				win[namespace].tab.init();

				if (!!$('.ui-inview').length) {
					$('.ui-inview').each(function(idx, item){
						win[namespace].isInview($(item));
					})
					win[namespace].skillAni.init(true);
					win[namespace].registerAni.init(true);
				}
				win[namespace].nav.hoverMenu(); // hover evt on nav
				win[namespace].nav.slidingMenu(); // show/hide evt on nav
				win[namespace].nav.openDepth2(); // 2depth links evt on nav

			})
			$(doc).on('scroll.'+namespace, function(){
				win[namespace].stepRolling.init();
			})
		}
	}
	
	win[namespace].init();
})(jQuery, window, document);


$(document).ready(function(){
	$('.floating-warp .btn-top-warp').on('click', function () {
		$('body, html').animate({
			scrollTop: 0
		}, 200)
	});
});

function go_family(url) {
	if (url) {
		window.open(url);
	}
}