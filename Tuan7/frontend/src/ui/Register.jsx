import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { api } from '../api/client.js'
import toast from 'react-hot-toast'
import { UserPlus, User, Lock, Shield, ArrowRight } from 'lucide-react'

export default function Register() {
  const nav = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [role, setRole] = useState('USER')
  const [loading, setLoading] = useState(false)

  async function submit(e) {
    e.preventDefault()
    setLoading(true)
    const tid = toast.loading('Đang xử lý đăng ký...')
    try {
      await api.post('/api/users/register', { username, password, role })
      toast.success('Đăng ký thành công! Đang chuyển hướng...', { id: tid })
      setTimeout(() => nav('/login'), 1000)
    } catch (e2) {
      toast.error(e2?.response?.data?.message || 'Đăng ký thất bại', { id: tid })
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
            background: 'rgba(168, 85, 247, 0.1)', 
            borderRadius: '16px', 
            display: 'flex', 
            alignItems: 'center', 
            justifyContent: 'center', 
            margin: '0 auto 1rem',
            color: 'var(--accent-secondary)'
          }}>
            <UserPlus size={32} />
          </div>
          <h2>Tạo tài khoản mới</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Tham gia cùng hệ thống Mini Food</p>
        </div>

        <form onSubmit={submit} className="form">
          <div className="form-group">
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <User size={14} /> Tên đăng nhập
            </label>
            <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Nhập username mong muốn" required />
          </div>

          <div className="form-group">
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Lock size={14} /> Mật khẩu
            </label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Nhập mật khẩu" required />
          </div>

          <div className="form-group">
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Shield size={14} /> Vai trò
            </label>
            <select className="form select" value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="USER">Người dùng (USER)</option>
              <option value="ADMIN">Quản trị viên (ADMIN)</option>
            </select>
          </div>

          <button className="btn primary" disabled={loading} style={{ marginTop: '1rem', padding: '1rem', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px' }}>
            {loading ? 'Đang xử lý...' : <>Đăng ký tài khoản <ArrowRight size={18} /></>}
          </button>
        </form>
        
        <p className="hint">
          Đã có tài khoản? <Link to="/login" className="accent-primary">Đăng nhập</Link>
        </p>
      </div>
    </div>
  )
}
