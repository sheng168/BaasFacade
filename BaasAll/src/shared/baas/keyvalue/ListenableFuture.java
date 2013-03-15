package shared.baas.keyvalue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface ListenableFuture<V> extends Future<V> {
	public static interface Listener<V> {
		void done(V value, Exception e);
	}
	
	void setListener(Listener<V> listener);
	
	public static class Basic<V> implements ListenableFuture<V> {
		boolean set = false;
		V value;
		Listener<V> listener;
		private Exception e;
		

		@Override
		public synchronized void setListener(Listener<V> listener) {
			this.listener = listener;
			if (set) {
				notifyListener();
			}
		}		

		public synchronized void set(V value, Exception e) {
			this.value = value;
			this.e = e;
			set = true;
			
			this.notifyAll(); // blocking on get			
			notifyListener();
		}

		protected void notifyListener() {
			if (listener != null) {
				listener.done(value, e);
			}
		}

		@Override
		public synchronized boolean isDone() {
			return set;
		}

		protected V result() throws ExecutionException {
			if (e != null) {
				throw new ExecutionException(e);
			} else {
				return value;
			}
		}

		@Override
		public synchronized V get() throws InterruptedException, ExecutionException {
			while (!set) {
				this.wait();
			}
			return result();
		}

		@Override
		public synchronized V get(long timeout, TimeUnit unit) throws InterruptedException,
				ExecutionException, TimeoutException {
			while (!set) {
				this.wait(unit.toMillis(timeout));
			}			
			return result();
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return false; // can't cancel
		}

		@Override
		public boolean isCancelled() {
			return false;
		}
	}
}
