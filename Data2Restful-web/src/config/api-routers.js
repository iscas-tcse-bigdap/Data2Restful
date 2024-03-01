function buildApiRouters(properties) {
  return [
    {
      url: '/AuthenticationManager',
      base: properties.url.auth,
      withCredentials: true
    },
    {
      url: '/AuthenticationManager/oauth',
      base: properties.url.auth,
      withCredentials: true,
      token: properties.token
    },
    {
      url: 'api/',
      base: properties.url.api,
      withCredentials: false,
      authorized: true
    },
    {
      url: 'k8scli/',
      base: properties.url.k8scli,
      withCredentials: false,
      authorized: true
    },
    {
      url: 'user/',
      base: properties.url.userauth,
      withCredentials: false,
      authorized: true
    },
  ]
}
export default buildApiRouters
