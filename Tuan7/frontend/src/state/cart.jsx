import { createContext, useContext, useMemo, useState } from 'react'

const CartCtx = createContext(null)

export function CartProvider({ children }) {
  const [items, setItems] = useState([])

  const value = useMemo(
    () => ({
      items,
      add: (food) => {
        setItems((prev) => {
          const idx = prev.findIndex((x) => x.foodId === food.id)
          if (idx >= 0) {
            const next = [...prev]
            next[idx] = { ...next[idx], quantity: next[idx].quantity + 1 }
            return next
          }
          return [...prev, { foodId: food.id, name: food.name, price: food.price, quantity: 1 }]
        })
      },
      remove: (foodId) => setItems((prev) => prev.filter((x) => x.foodId !== foodId)),
      clear: () => setItems([]),
      setQty: (foodId, quantity) =>
        setItems((prev) => prev.map((x) => (x.foodId === foodId ? { ...x, quantity } : x))),
    }),
    [items],
  )

  return <CartCtx.Provider value={value}>{children}</CartCtx.Provider>
}

export function useCart() {
  const v = useContext(CartCtx)
  if (!v) throw new Error('useCart must be used within CartProvider')
  return v
}

