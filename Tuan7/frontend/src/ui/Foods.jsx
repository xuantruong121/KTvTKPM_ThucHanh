import { useEffect, useMemo, useState } from 'react'
import { api } from '../api/client.js'
import { useCart } from '../state/cart.jsx'
import { useAuth } from '../state/auth.jsx'
import toast from 'react-hot-toast'
import { Search, Plus, UtensilsCrossed, Loader2 } from 'lucide-react'

export default function Foods() {
  const { add } = useCart()
  const { user } = useAuth()
  const [foods, setFoods] = useState([])
  const [loading, setLoading] = useState(true)
  const [err, setErr] = useState(null)
  const [q, setQ] = useState('')

  useEffect(() => {
    let alive = true
    setLoading(true)
    api
      .get('/api/foods')
      .then((r) => {
        if (!alive) return
        setFoods(r.data)
        setErr(null)
      })
      .catch((e) => alive && setErr(e?.response?.data?.message || 'Khong tai duoc danh sach mon an'))
      .finally(() => alive && setLoading(false))
    return () => {
      alive = false
    }
  }, [])

  const filtered = useMemo(() => {
    const s = q.trim().toLowerCase()
    if (!s) return foods
    return foods.filter((f) => String(f.name || '').toLowerCase().includes(s))
  }, [foods, q])

  const handleAdd = (food) => {
    add(food)
    toast.success(`Đã thêm ${food.name} vào giỏ!`, { icon: '🛒' })
  }

  return (
    <div className="fade-in">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <h1 style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <UtensilsCrossed className="accent-primary" />
            Thực đơn hôm nay
          </h1>
          <p style={{ color: 'var(--text-muted)' }}>
            {loading ? 'Đang cập nhật thực đơn...' : `Khám phá ${filtered.length} món ăn hấp dẫn`}
            {!user && ' — Đăng nhập để đặt món ngay!'}
          </p>
        </div>
        
        <div style={{ position: 'relative', width: '100%', maxWidth: '300px' }}>
          <Search size={18} style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
          <input
            className="form input"
            style={{ width: '100%', paddingLeft: '40px' }}
            value={q}
            onChange={(e) => setQ(e.target.value)}
            placeholder="Tìm kiếm món ăn..."
          />
        </div>
      </div>

      {err && <div className="error">{err}</div>}

      <div className="items-grid">
        {loading ? (
          Array.from({ length: 6 }).map((_, idx) => (
            <div key={idx} className="item-card skeleton-card" style={{ height: '200px' }}>
              <Loader2 className="spin" style={{ margin: 'auto' }} />
            </div>
          ))
        ) : (
          filtered.map((f) => (
            <div key={f.id} className="item-card">
              <div style={{ fontSize: '1.25rem', fontWeight: '700', marginBottom: '0.5rem' }}>{f.name}</div>
              <div className="price" style={{ marginBottom: '1.5rem' }}>
                {Number(f.price).toLocaleString('vi-VN')} <span style={{ fontSize: '0.9rem' }}>₫</span>
              </div>
              <button 
                className={`btn ${user ? 'primary' : ''}`} 
                style={{ width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px' }}
                onClick={() => handleAdd(f)} 
                disabled={!user}
              >
                {user ? <><Plus size={18} /> Thêm vào giỏ</> : 'Đăng nhập để đặt'}
              </button>
            </div>
          ))
        )}
      </div>
      
      {filtered.length === 0 && !loading && (
        <div style={{ textAlign: 'center', padding: '4rem 0', color: 'var(--text-muted)' }}>
          <Search size={48} style={{ opacity: 0.2, marginBottom: '1rem' }} />
          <p>Không tìm thấy món ăn nào phù hợp.</p>
        </div>
      )}

      {user?.role === 'ADMIN' && !loading && (
        <div className="card" style={{ marginTop: '3rem', background: 'rgba(99, 102, 241, 0.05)' }}>
          <p className="hint" style={{ textAlign: 'left', margin: 0 }}>
            ✨ <strong>ADMIN Mode:</strong> Bạn có quyền quản trị. Sử dụng các công cụ API để quản lý món ăn.
          </p>
        </div>
      )}
    </div>
  )
}

