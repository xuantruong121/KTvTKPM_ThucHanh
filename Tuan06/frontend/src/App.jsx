import { Link, Navigate, Route, Routes, useLocation } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import { ShoppingCart, LayoutDashboard, CreditCard, Utensils, LogOut, User } from 'lucide-react'
import { useAuth } from './state/auth.jsx'
import Login from './ui/Login.jsx'
import Register from './ui/Register.jsx'
import Foods from './ui/Foods.jsx'
import Cart from './ui/Cart.jsx'
import Orders from './ui/Orders.jsx'
import Pay from './ui/Pay.jsx'
import './App.css'

function RequireAuth({ children }) {
  const { accessToken } = useAuth()
  const loc = useLocation()
  if (!accessToken) return <Navigate to="/login" replace state={{ from: loc.pathname }} />
  return children
}

export default function App() {
  const { user, logout } = useAuth()
  const loc = useLocation()

  return (
    <div className="shell">
      <Toaster position="top-right" reverseOrder={false} />
      <header className="topbar">
        <div className="brand">Mini Food Ordering</div>
        <nav className="nav">
          <Link to="/foods" className={loc.pathname === '/foods' ? 'active' : ''}>
            <Utensils size={18} />
            Món ăn
          </Link>
          <Link to="/cart" className={loc.pathname === '/cart' ? 'active' : ''}>
            <ShoppingCart size={18} />
            Giỏ hàng
          </Link>
          <Link to="/orders" className={loc.pathname === '/orders' ? 'active' : ''}>
            <LayoutDashboard size={18} />
            Đơn hàng
          </Link>
          <Link to="/pay" className={loc.pathname === '/pay' ? 'active' : ''}>
            <CreditCard size={18} />
            Thanh toán
          </Link>
        </nav>
        <div className="auth">
          {user ? (
            <>
              <span className="pill">
                <User size={14} style={{ verticalAlign: 'middle', marginRight: '4px' }} />
                {user.username} ({user.role})
              </span>
              <button className="btn" onClick={logout}>
                <LogOut size={16} />
              </button>
            </>
          ) : (
            <>
              <Link className="btn" to="/login">
                Login
              </Link>
              <Link className="btn primary" to="/register">
                Register
              </Link>
            </>
          )}
        </div>
      </header>

      <main className="content">
        <Routes>
          <Route path="/" element={<Navigate to="/foods" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/foods" element={<Foods />} />
          <Route
            path="/cart"
            element={
              <RequireAuth>
                <Cart />
              </RequireAuth>
            }
          />
          <Route
            path="/orders"
            element={
              <RequireAuth>
                <Orders />
              </RequireAuth>
            }
          />
          <Route
            path="/pay"
            element={
              <RequireAuth>
                <Pay />
              </RequireAuth>
            }
          />
          <Route path="*" element={<div>Not found</div>} />
        </Routes>
      </main>
    </div>
  )
}
