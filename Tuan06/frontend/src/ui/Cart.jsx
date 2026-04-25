import { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../api/client.js'
import { useCart } from '../state/cart.jsx'
import toast from 'react-hot-toast'
import { ShoppingBag, Trash2, ArrowRight, CreditCard } from 'lucide-react'

export default function Cart() {
  const { items, remove, setQty, clear } = useCart()
  const [creating, setCreating] = useState(false)
  const navigate = useNavigate()

  const total = useMemo(
    () => items.reduce((sum, it) => sum + Number(it.price) * Number(it.quantity), 0),
    [items],
  )

  async function createOrder() {
    setCreating(true)
    const tid = toast.loading('Đang khởi tạo đơn hàng...')
    try {
      const payload = { items: items.map((x) => ({ foodId: x.foodId, quantity: x.quantity })) }
      const res = await api.post('/api/orders', payload)
      toast.success(`Đã tạo đơn hàng #${res.data.id}!`, { id: tid })
      clear()
      // Redirect to payment with orderId
      navigate(`/pay?orderId=${res.data.id}`)
    } catch (e) {
      toast.error(e?.response?.data?.message || 'Tạo đơn thất bại', { id: tid })
    } finally {
      setCreating(false)
    }
  }

  return (
    <div className="fade-in">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1 style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <ShoppingBag className="accent-primary" />
          Giỏ hàng của bạn
        </h1>
        {items.length > 0 && (
          <button className="btn" onClick={() => { clear(); toast.success('Đã xóa toàn bộ giỏ hàng'); }} disabled={creating}>
            <Trash2 size={16} style={{ marginRight: '6px' }} /> Xóa hết
          </button>
        )}
      </div>

      {items.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: '4rem 0' }}>
          <ShoppingBag size={64} style={{ opacity: 0.1, marginBottom: '1.5rem' }} />
          <p style={{ color: 'var(--text-muted)', fontSize: '1.1rem' }}>Giỏ hàng của bạn đang trống.</p>
          <button className="btn primary" onClick={() => navigate('/foods')} style={{ marginTop: '1.5rem' }}>
            Xem thực đơn ngay
          </button>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 350px', gap: '2rem', alignItems: 'start' }}>
          <div className="card" style={{ padding: '0' }}>
            {items.map((it, idx) => (
              <div key={it.foodId} style={{ 
                display: 'flex', 
                alignItems: 'center', 
                padding: '1.5rem', 
                borderBottom: idx === items.length - 1 ? 'none' : '1px solid var(--border)',
                gap: '1rem'
              }}>
                <div style={{ flex: 1 }}>
                  <div style={{ fontWeight: '700', fontSize: '1.1rem' }}>{it.name}</div>
                  <div style={{ color: 'var(--accent-primary)', fontWeight: '600' }}>
                    {Number(it.price).toLocaleString('vi-VN')} ₫
                  </div>
                </div>
                
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                  <input
                    className="form input"
                    style={{ width: '70px', textAlign: 'center' }}
                    type="number"
                    min="1"
                    value={it.quantity}
                    onChange={(e) => setQty(it.foodId, Math.max(1, Number(e.target.value || 1)))}
                  />
                  <div style={{ width: '120px', textAlign: 'right', fontWeight: '700' }}>
                    {(Number(it.price) * it.quantity).toLocaleString('vi-VN')} ₫
                  </div>
                  <button className="btn" onClick={() => remove(it.foodId)} style={{ padding: '0.5rem', color: 'var(--error)' }}>
                    <Trash2 size={18} />
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="card" style={{ position: 'sticky', top: '100px' }}>
            <h3 style={{ marginBottom: '1.5rem' }}>Tóm tắt đơn hàng</h3>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '1rem' }}>
              <span style={{ color: 'var(--text-muted)' }}>Tạm tính</span>
              <span>{total.toLocaleString('vi-VN')} ₫</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '2rem', fontSize: '1.25rem', fontWeight: '800', borderTop: '1px solid var(--border)', paddingTop: '1.5rem' }}>
              <span>Tổng cộng</span>
              <span className="accent-primary">{total.toLocaleString('vi-VN')} ₫</span>
            </div>
            
            <button 
              className="btn primary" 
              style={{ width: '100%', padding: '1rem', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '10px' }}
              onClick={createOrder} 
              disabled={creating}
            >
              {creating ? 'Đang xử lý...' : <><CreditCard size={18} /> Tiến hành đặt hàng <ArrowRight size={18} /></>}
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
