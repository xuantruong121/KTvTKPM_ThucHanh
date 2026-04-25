import { createContext, useContext, useMemo, useState } from 'react'

const AuthCtx = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('user')
    return raw ? JSON.parse(raw) : null
  })
  const [accessToken, setAccessToken] = useState(() => localStorage.getItem('accessToken'))

  const value = useMemo(
    () => ({
      user,
      accessToken,
      setSession: (token, u) => {
        localStorage.setItem('accessToken', token)
        localStorage.setItem('user', JSON.stringify(u))
        setAccessToken(token)
        setUser(u)
      },
      logout: () => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('user')
        setAccessToken(null)
        setUser(null)
      },
    }),
    [user, accessToken],
  )

  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>
}

export function useAuth() {
  const v = useContext(AuthCtx)
  if (!v) throw new Error('useAuth must be used within AuthProvider')
  return v
}

