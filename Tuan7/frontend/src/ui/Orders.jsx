import { useEffect, useState } from 'react'
import { api } from '../api/client.js'
import { Link } from 'react-router-dom'
import toast from 'react-hot-toast'
import { ClipboardList, RefreshCw, CreditCard, ChevronRight } from 'lucide-react'

export default function Orders() {
  const [orders, setOrders] = useState([])
  const [err, setErr] = useState(null)
  const [loading, setLoading] = useState(true)

  async function load() {
    setLoading(true)
    setErr(null)
    try {
      const r = await api.get('/api/orders')
      setOrders(r.data)
    } catch (e) {
      const msg = e?.response?.data?.message || 'Không tải được đơn hàng'
      setErr(msg)
      toast.error(msg)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <div className="fade-in">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1 style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <ClipboardList className="accent-primary" />
          Lịch sử đơn hàng
        </h1>
        <div style={{ display: 'flex', gap: '10px' }}>
          <button className="btn" onClick={() => { load(); toast.success('Đã làm mới danh sách'); }} disabled={loading}>
            <RefreshCw size={18} className={loading ? 'spin' : ''} />
          </button>
          <Link className="btn primary" to="/pay" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <CreditCard size={18} /> Thanh toán tiếp
          </Link>
        </div>
      </div>

      {err && <div className="error">{err}</div>}

      {loading ? (
        <div style={{ textAlign: 'center', padding: '4rem' }}>
          <RefreshCw size={48} className="spin" style={{ opacity: 0.2 }} />
          <p style={{ marginTop: '1rem', color: 'var(--text-muted)' }}>Đang tải đơn hàng...</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
          {orders.map((o) => (
            <div key={o.id} className="card" style={{ padding: '0', overflow: 'hidden' }}>
              <div style={{ 
                padding: '1.25rem 1.5rem', 
                background: 'rgba(255,255,255,0.03)', 
                borderBottom: '1px solid var(--border)',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center'
              }}>
                <div>
                  <span style={{ fontSize: '1.1rem', fontWeight: '800' }}>Đơn hàng #{o.id}</span>
                  <span style={{ marginLeft: '1rem' }} className={`status-badge status-${o.status}`}>
                    {o.status}
                  </span>
                </div>
                <div style={{ fontWeight: '800', fontSize: '1.1rem' }} className="accent-primary">
                  {Number(o.totalAmount).toLocaleString('vi-VN')} ₫
                </div>
              </div>
              
              <div style={{ padding: '1.5rem' }}>
                <div style={{ marginBottom: '1rem', fontSize: '0.9rem', color: 'var(--text-muted)' }}>
                  Chi tiết món ăn:
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                  {o.items.map((it) => (
                    <div key={it.foodId} style={{ display: 'flex', justifyContent: 'space-between' }}>
                      <span>
                        <ChevronRight size={14} style={{ verticalAlign: 'middle', marginRight: '4px' }} />
                        {it.foodName} <span style={{ color: 'var(--text-muted)' }}>× {it.quantity}</span>
                      </span>
                      <span style={{ fontWeight: '600' }}>{Number(it.lineTotal).toLocaleString('vi-VN')} ₫</span>
                    </div>
                  ))}
                </div>
                
                {o.status === 'PENDING' && (
                  <div style={{ marginTop: '2rem', borderTop: '1px solid var(--border)', paddingTop: '1.5rem', textAlign: 'right' }}>
                    <Link to={`/pay?orderId=${o.id}`} className="btn primary" style={{ display: 'inline-flex', alignItems: 'center', gap: '8px' }}>
                      <CreditCard size={16} /> Thanh toán ngay
                    </Link>
                  </div>
                )}
              </div>
            </div>
          ))}
          
          {orders.length === 0 && (
            <div className="card" style={{ textAlign: 'center', padding: '4rem' }}>
              <ClipboardList size={64} style={{ opacity: 0.1, marginBottom: '1.5rem' }} />
              <p style={{ color: 'var(--text-muted)' }}>Bạn chưa có đơn hàng nào.</p>
            </div>
          )}
        </div>
      )}
    </div>
  )
}
