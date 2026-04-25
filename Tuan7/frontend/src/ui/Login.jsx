import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { api } from '../api/client.js'
import { useAuth } from '../state/auth.jsx'
import toast from 'react-hot-toast'
import { LogIn, User, Lock, ArrowRight } from 'lucide-react'

export default function Login() {
  const nav = useNavigate()
  const { setSession } = useAuth()
  const [username, setUsername] = useState('user')
  const [password, setPassword] = useState('user123')
  const [loading, setLoading] = useState(false)

  async function submit(e) {
    e.preventDefault()
    setLoading(true)
    const tid = toast.loading('Đang xác thực...')
    try {
      const res = await api.post('/api/users/login', { username, password })
      setSession(res.data.accessToken, res.data.user)
      toast.success(`Chào mừng ${res.data.user.username} quay trở lại!`, { id: tid })
      nav('/foods')
    } catch (e2) {
      toast.error(e2?.response?.data?.message || 'Đăng nhập thất bại', { id: tid })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ maxWidth: '450px', margin: '4rem auto' }}>
      <div className="card fade-in">
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{ 
            width: '64px', 
            height: '64px', 
            background: 'rgba(99, 102, 241, 0.1)', 
            borderRadius: '16px', 
            display: 'flex', 
            alignItems: 'center', 
            justifyContent: 'center', 
            margin: '0 auto 1rem',
            color: 'var(--accent-primary)'
          }}>
            <LogIn size={32} />
          </div>
          <h2>Đăng nhập</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Vui lòng nhập thông tin tài khoản của bạn</p>
        </div>

        <form onSubmit={submit} className="form">
          <div className="form-group">
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <User size={14} /> Tên đăng nhập
            </label>
            <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Nhập username" required />
          </div>

          <div className="form-group">
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Lock size={14} /> Mật khẩu
            </label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Nhập password" required />
          </div>

          <button className="btn primary" disabled={loading} style={{ marginTop: '1rem', padding: '1rem', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px' }}>
            {loading ? 'Đang xử lý...' : <>Đăng nhập <ArrowRight size={18} /></>}
          </button>
        </form>
        
        <p className="hint">
          Chưa có tài khoản? <Link to="/register" className="accent-primary">Đăng ký ngay</Link>
        </p>
        <p className="hint" style={{ fontSize: '0.75rem', opacity: 0.7 }}>
          * Tài khoản demo: <code>user/user123</code> hoặc <code>admin/admin123</code>
        </p>
      </div>
    </div>
  )
}
