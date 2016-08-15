let SessionLoad = 1
if &cp | set nocp | endif
let s:cpo_save=&cpo
set cpo&vim
map! <S-Insert> *
map Q gq
map \rwp <Plug>RestoreWinPosn
map \swp <Plug>SaveWinPosn
nmap \scp <Plug>SQLUCreateProcedure
nmap \scdt <Plug>SQLUGetColumnDataType
nmap \scd <Plug>SQLUGetColumnDef
nmap \scl <Plug>SQLUCreateColumnList
vmap \sf <Plug>SQLUFormatter
nmap \sf <Plug>SQLUFormatter
vmap \sfs <Plug>SQLUFormatter
nmap \sfs <Plug>SQLUFormatter
vmap \sfr <Plug>SQLUFormatStmts
nmap \sfr <Plug>SQLUFormatStmts
vmap <silent> \tt :call AlignMaps#Vis("tt")
nmap \tt <Plug>AM_tt
vmap <silent> \tsq :call AlignMaps#Vis("tsq")
nmap \tsq <Plug>AM_tsq
vmap <silent> \tsp :call AlignMaps#Vis("tsp")
nmap \tsp <Plug>AM_tsp
vmap <silent> \tml :call AlignMaps#Vis("tml")
nmap \tml <Plug>AM_tml
vmap <silent> \tab :call AlignMaps#Vis("tab")
nmap \tab <Plug>AM_tab
vmap <silent> \m= :call AlignMaps#Vis("m=")
nmap \m= <Plug>AM_m=
vmap <silent> \tW@ :call AlignMaps#Vis("tW@")
nmap \tW@ <Plug>AM_tW@
vmap <silent> \t@ :call AlignMaps#Vis("t@")
nmap \t@ <Plug>AM_t@
vmap <silent> \t~ :call AlignMaps#Vis("t~")
nmap \t~ <Plug>AM_t~
vmap <silent> \t? :call AlignMaps#Vis("t?")
nmap \t? <Plug>AM_t?
vmap <silent> \w= :call AlignMaps#Vis("w=")
nmap \w= <Plug>AM_w=
vmap <silent> \ts= :call AlignMaps#Vis("ts=")
nmap \ts= <Plug>AM_ts=
vmap <silent> \ts< :call AlignMaps#Vis("ts<")
nmap \ts< <Plug>AM_ts<
vmap <silent> \ts; :call AlignMaps#Vis("ts;")
nmap \ts; <Plug>AM_ts;
vmap <silent> \ts: :call AlignMaps#Vis("ts:")
nmap \ts: <Plug>AM_ts:
vmap <silent> \ts, :call AlignMaps#Vis("ts,")
nmap \ts, <Plug>AM_ts,
vmap <silent> \t= :call AlignMaps#Vis("t=")
nmap \t= <Plug>AM_t=
vmap <silent> \t< :call AlignMaps#Vis("t<")
nmap \t< <Plug>AM_t<
vmap <silent> \t; :call AlignMaps#Vis("t;")
nmap \t; <Plug>AM_t;
vmap <silent> \t: :call AlignMaps#Vis("t:")
nmap \t: <Plug>AM_t:
vmap <silent> \t, :call AlignMaps#Vis("t,")
nmap \t, <Plug>AM_t,
vmap <silent> \t# :call AlignMaps#Vis("t#")
nmap \t# <Plug>AM_t#
vmap <silent> \t| :call AlignMaps#Vis("t|")
nmap \t| <Plug>AM_t|
vmap <silent> \T~ :call AlignMaps#Vis("T~")
nmap \T~ <Plug>AM_T~
vmap <silent> \Tsp :call AlignMaps#Vis("Tsp")
nmap \Tsp <Plug>AM_Tsp
vmap <silent> \Tab :call AlignMaps#Vis("Tab")
nmap \Tab <Plug>AM_Tab
vmap <silent> \TW@ :call AlignMaps#Vis("TW@")
nmap \TW@ <Plug>AM_TW@
vmap <silent> \T@ :call AlignMaps#Vis("T@")
nmap \T@ <Plug>AM_T@
vmap <silent> \T? :call AlignMaps#Vis("T?")
nmap \T? <Plug>AM_T?
vmap <silent> \T= :call AlignMaps#Vis("T=")
nmap \T= <Plug>AM_T=
vmap <silent> \T< :call AlignMaps#Vis("T<")
nmap \T< <Plug>AM_T<
vmap <silent> \T; :call AlignMaps#Vis("T;")
nmap \T; <Plug>AM_T;
vmap <silent> \T: :call AlignMaps#Vis("T:")
nmap \T: <Plug>AM_T:
vmap <silent> \Ts, :call AlignMaps#Vis("Ts,")
nmap \Ts, <Plug>AM_Ts,
vmap <silent> \T, :call AlignMaps#Vis("T,")
nmap \T, <Plug>AM_T,
vmap <silent> \T# :call AlignMaps#Vis("T#")
nmap \T# <Plug>AM_T#
vmap <silent> \T| :call AlignMaps#Vis("T|")
nmap \T| <Plug>AM_T|
map \Htd <Plug>AM_Htd
vmap <silent> \anum :call AlignMaps#Vis("anum")
nmap \anum <Plug>AM_anum
vmap <silent> \aenum :call AlignMaps#Vis("aenum")
nmap \aenum <Plug>AM_aenum
vmap <silent> \aunum :call AlignMaps#Vis("aunum")
nmap \aunum <Plug>AM_aunum
vmap <silent> \afnc :call AlignMaps#Vis("afnc")
nmap \afnc <Plug>AM_afnc
vmap <silent> \adef :call AlignMaps#Vis("adef")
nmap \adef <Plug>AM_adef
vmap <silent> \adec :call AlignMaps#Vis("adec")
nmap \adec <Plug>AM_adec
vmap <silent> \ascom :call AlignMaps#Vis("ascom")
nmap \ascom <Plug>AM_ascom
vmap <silent> \aocom :call AlignMaps#Vis("aocom")
nmap \aocom <Plug>AM_aocom
vmap <silent> \adcom :call AlignMaps#Vis("adcom")
nmap \adcom <Plug>AM_adcom
vmap <silent> \acom :call AlignMaps#Vis("acom")
nmap \acom <Plug>AM_acom
vmap <silent> \abox :call AlignMaps#Vis("abox")
nmap \abox <Plug>AM_abox
vmap <silent> \a( :call AlignMaps#Vis("a(")
nmap \a( <Plug>AM_a(
vmap <silent> \a= :call AlignMaps#Vis("a=")
nmap \a= <Plug>AM_a=
vmap <silent> \a< :call AlignMaps#Vis("a<")
nmap \a< <Plug>AM_a<
vmap <silent> \a, :call AlignMaps#Vis("a,")
nmap \a, <Plug>AM_a,
vmap <silent> \a? :call AlignMaps#Vis("a?")
nmap \a? <Plug>AM_a?
vmap gx <Plug>NetrwBrowseXVis
nmap gx <Plug>NetrwBrowseX
vnoremap <silent> <Plug>NetrwBrowseXVis :call netrw#BrowseXVis()
nnoremap <silent> <Plug>NetrwBrowseX :call netrw#BrowseX(expand((exists("g:netrw_gx")? g:netrw_gx : '<cfile>')),netrw#CheckIfRemote())
nmap <silent> <Plug>RestoreWinPosn :call RestoreWinPosn()
nmap <silent> <Plug>SaveWinPosn :call SaveWinPosn()
nmap <SNR>30_WE <Plug>AlignMapsWrapperEnd
map <SNR>30_WS <Plug>AlignMapsWrapperStart
nmap <F5> :PymodeRun
nmap <F8> :TagbarToggle
let &cpo=s:cpo_save
unlet s:cpo_save
set autoindent
set background=dark
set backspace=2
set backupdir=~/.vim/_backups//
set directory=~/.vim/_swaps//
set expandtab
set fileencodings=ucs-bom,utf-8,latin1
set guifont=Droid\ Sans\ Mono\ 14
set helplang=en
set hlsearch
set iminsert=0
set langmenu=en_US.UTF-8
set mouse=a
set mousemodel=popup
set ruler
set runtimepath=~/.vim,~/.vim/bundle/vim-airline/,~/.vim/bundle/tagbar/,~/.vim/bundle/python-mode/,~/.vim/bundle/.neobundle,/usr/share/vim/vimfiles,/usr/share/vim/vim74,/usr/share/vim/vimfiles/after,~/.vim/after,~/.vim/bundle/neobundle.vim/,~/.vim/bundle/python-mode/after
set shiftwidth=2
set suffixes=.bak,~,.o,.h,.info,.swp,.obj,.info,.aux,.log,.dvi,.bbl,.out,.o,.lo
set tabstop=2
set termencoding=utf-8
set window=41
let s:so_save = &so | let s:siso_save = &siso | set so=0 siso=0
let v:this_session=expand("<sfile>:p")
silent only
cd ~/Projects/TurboInternational/metadata/mockup
if expand('%') == '' && !&modified && line('$') <= 1 && getline(1) == ''
  let s:wipebuf = bufnr('%')
endif
set shortmess=aoO
badd +61 index.html
badd +1 js/script.js
badd +6 views/PartSearch.html
argglobal
silent! argdel *
argadd index.html
argadd js/script.js
edit views/PartSearch.html
set splitbelow splitright
wincmd _ | wincmd |
split
1wincmd k
wincmd w
set nosplitbelow
set nosplitright
wincmd t
set winheight=1 winwidth=1
exe '1resize ' . ((&lines * 17 + 21) / 42)
exe '2resize ' . ((&lines * 22 + 21) / 42)
argglobal
edit views/PartSearch.html
setlocal keymap=
setlocal noarabic
setlocal autoindent
setlocal backupcopy=
setlocal balloonexpr=
setlocal nobinary
setlocal nobreakindent
setlocal breakindentopt=
setlocal bufhidden=
setlocal buflisted
setlocal buftype=
setlocal nocindent
setlocal cinkeys=0{,0},0),:,0#,!^F,o,O,e
setlocal cinoptions=j1,J1
setlocal cinwords=if,else,while,do,for,switch
set colorcolumn=80
setlocal colorcolumn=80
setlocal comments=s:<!--,m:\ \ \ \ ,e:-->
setlocal commentstring=<!--%s-->
setlocal complete=.,w,b,u,t,i
setlocal concealcursor=
setlocal conceallevel=0
setlocal completefunc=
setlocal nocopyindent
setlocal cryptmethod=
setlocal nocursorbind
setlocal nocursorcolumn
set cursorline
setlocal cursorline
setlocal define=
setlocal dictionary=
setlocal nodiff
setlocal equalprg=
setlocal errorformat=
setlocal expandtab
if &filetype != 'html'
setlocal filetype=html
endif
setlocal foldcolumn=0
setlocal foldenable
setlocal foldexpr=0
setlocal foldignore=#
setlocal foldlevel=0
setlocal foldmarker={{{,}}}
setlocal foldmethod=manual
setlocal foldminlines=1
setlocal foldnestmax=20
setlocal foldtext=foldtext()
setlocal formatexpr=
setlocal formatoptions=tcq
setlocal formatlistpat=^\\s*\\d\\+[\\]:.)}\\t\ ]\\s*
setlocal grepprg=
setlocal iminsert=0
setlocal imsearch=2
setlocal include=
setlocal includeexpr=
setlocal indentexpr=HtmlIndent()
setlocal indentkeys=o,O,<Return>,<>>,{,},!^F
setlocal noinfercase
setlocal iskeyword=@,48-57,_,192-255
setlocal keywordprg=
setlocal nolinebreak
setlocal nolisp
setlocal lispwords=
set list
setlocal list
setlocal makeprg=
setlocal matchpairs=(:),{:},[:],<:>
setlocal modeline
setlocal modifiable
setlocal nrformats=octal,hex
set number
setlocal number
setlocal numberwidth=4
setlocal omnifunc=htmlcomplete#CompleteTags
setlocal path=
setlocal nopreserveindent
setlocal nopreviewwindow
setlocal quoteescape=\\
setlocal noreadonly
setlocal norelativenumber
setlocal norightleft
setlocal rightleftcmd=search
setlocal noscrollbind
setlocal shiftwidth=4
setlocal noshortname
setlocal nosmartindent
setlocal softtabstop=0
setlocal nospell
setlocal spellcapcheck=[.?!]\\_[\\])'\"\	\ ]\\+
setlocal spellfile=
setlocal spelllang=en
setlocal statusline=%!airline#statusline(1)
setlocal suffixesadd=
setlocal swapfile
setlocal synmaxcol=3000
if &syntax != 'html'
setlocal syntax=html
endif
setlocal tabstop=4
setlocal tags=
setlocal textwidth=0
setlocal thesaurus=
setlocal noundofile
setlocal undolevels=-123456
setlocal nowinfixheight
setlocal nowinfixwidth
setlocal wrap
setlocal wrapmargin=0
silent! normal! zE
let s:l = 1 - ((0 * winheight(0) + 8) / 17)
if s:l < 1 | let s:l = 1 | endif
exe s:l
normal! zt
1
normal! 0
wincmd w
argglobal
edit js/script.js
setlocal keymap=
setlocal noarabic
setlocal autoindent
setlocal backupcopy=
setlocal balloonexpr=
setlocal nobinary
setlocal nobreakindent
setlocal breakindentopt=
setlocal bufhidden=
setlocal buflisted
setlocal buftype=
setlocal cindent
setlocal cinkeys=0{,0},0),:,!^F,o,O,e,0]
setlocal cinoptions=j1,J1
setlocal cinwords=if,else,while,do,for,switch
set colorcolumn=80
setlocal colorcolumn=80
setlocal comments=sO:*\ -,mO:*\ \ ,exO:*/,s1:/*,mb:*,ex:*/,://
setlocal commentstring=//%s
setlocal complete=.,w,b,u,t,i
setlocal concealcursor=
setlocal conceallevel=0
setlocal completefunc=
setlocal nocopyindent
setlocal cryptmethod=
setlocal nocursorbind
setlocal nocursorcolumn
set cursorline
setlocal cursorline
setlocal define=
setlocal dictionary=
setlocal nodiff
setlocal equalprg=
setlocal errorformat=
setlocal expandtab
if &filetype != 'javascript'
setlocal filetype=javascript
endif
setlocal foldcolumn=0
setlocal foldenable
setlocal foldexpr=0
setlocal foldignore=#
setlocal foldlevel=0
setlocal foldmarker={{{,}}}
setlocal foldmethod=manual
setlocal foldminlines=1
setlocal foldnestmax=20
setlocal foldtext=foldtext()
setlocal formatexpr=
setlocal formatoptions=croql
setlocal formatlistpat=^\\s*\\d\\+[\\]:.)}\\t\ ]\\s*
setlocal grepprg=
setlocal iminsert=0
setlocal imsearch=2
setlocal include=
setlocal includeexpr=
setlocal indentexpr=
setlocal indentkeys=0{,0},:,0#,!^F,o,O,e
setlocal noinfercase
setlocal iskeyword=@,48-57,_,192-255
setlocal keywordprg=
setlocal nolinebreak
setlocal nolisp
setlocal lispwords=
set list
setlocal list
setlocal makeprg=
setlocal matchpairs=(:),{:},[:]
setlocal modeline
setlocal modifiable
setlocal nrformats=octal,hex
set number
setlocal number
setlocal numberwidth=4
setlocal omnifunc=javascriptcomplete#CompleteJS
setlocal path=
setlocal nopreserveindent
setlocal nopreviewwindow
setlocal quoteescape=\\
setlocal noreadonly
setlocal norelativenumber
setlocal norightleft
setlocal rightleftcmd=search
setlocal noscrollbind
setlocal shiftwidth=2
setlocal noshortname
setlocal nosmartindent
setlocal softtabstop=0
setlocal nospell
setlocal spellcapcheck=[.?!]\\_[\\])'\"\	\ ]\\+
setlocal spellfile=
setlocal spelllang=en
setlocal statusline=%!airline#statusline(2)
setlocal suffixesadd=
setlocal swapfile
setlocal synmaxcol=3000
if &syntax != 'javascript'
setlocal syntax=javascript
endif
setlocal tabstop=2
setlocal tags=
setlocal textwidth=0
setlocal thesaurus=
setlocal noundofile
setlocal undolevels=-123456
setlocal nowinfixheight
setlocal nowinfixwidth
setlocal wrap
setlocal wrapmargin=0
silent! normal! zE
let s:l = 18 - ((17 * winheight(0) + 11) / 22)
if s:l < 1 | let s:l = 1 | endif
exe s:l
normal! zt
18
normal! 025|
wincmd w
2wincmd w
exe '1resize ' . ((&lines * 17 + 21) / 42)
exe '2resize ' . ((&lines * 22 + 21) / 42)
tabnext 1
if exists('s:wipebuf')
  silent exe 'bwipe ' . s:wipebuf
endif
unlet! s:wipebuf
set winheight=1 winwidth=20 shortmess=filnxtToO
let s:sx = expand("<sfile>:p:r")."x.vim"
if file_readable(s:sx)
  exe "source " . fnameescape(s:sx)
endif
let &so = s:so_save | let &siso = s:siso_save
doautoall SessionLoadPost
unlet SessionLoad
" vim: set ft=vim :
