function accept_cookies(){
    var cookie_name = "micromappers_accept_cookies";
    var exdays = 120;
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookie_value = escape("Yes") + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
    document.cookie = cookie_name + "=" + cookie_value + ";path=/";
};
