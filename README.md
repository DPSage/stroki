
|API|Permissions|
|:---:|:---------:|
|/{short-link}|permit all|
|/login|permit all|
|/api/user/registration|permit all|
|/api/links|hasAnyAuthority("ROLE_USER")|
|/api/link/add|hasAnyAuthority("ROLE_USER")|
|/api/link/{short-link}/info|hasAnyAuthority("ROLE_USER")|
|/api/link/{short-link}/delete|hasAnyAuthority("ROLE_USER")|
|/api/user/info|hasAnyAuthority("ROLE_USER")|
|/api/roles|hasAnyAuthority("ROLE_ADMIN")|
|/api/role/add|hasAnyAuthority("ROLE_ADMIN")|
|/api/users|hasAnyAuthority("ROLE_ADMIN")|
|/api/user/add-role|hasAnyAuthority("ROLE_ADMIN")|
