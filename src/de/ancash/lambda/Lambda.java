package de.ancash.lambda;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Lambda<T> {

	public static <T> Predicate<T> notNull() {
		return t -> t != null;
	}
	
	public static <T> Lambda<T> of(T t) {
		return new Lambda<T>(t);
	}
	
	public static <T> Lambda<T> of(Supplier<T> t) {
		Objects.requireNonNull(t);
		return new Lambda<T>(t.get());
	}
	
	public static void execIf(boolean b, Runnable r) {
		Objects.requireNonNull(r);
		if(b)
			r.run();
	}
	
	public static void execIf(Supplier<Boolean> b, Runnable r) {
		Objects.requireNonNull(b);
		Objects.requireNonNull(r);
		if(b.get())
			r.run();
	}
	
	private T val;
	
	Lambda(T t) {
		this.val = t;
	}
	
	public T get() {
		return val;
	}
	
	public boolean isPresent() {
		return val != null;
	}
	
	public <R> Lambda<R> map(Function<T, R> f) {
		Objects.requireNonNull(f);
		return Lambda.of(f.apply(val));
	}
	
	public <R> Lambda<R> flatMap(Function<? super T, Lambda<R>> mapper) {
		Objects.requireNonNull(mapper);
        if (!isPresent())
            return new Lambda<R>(null);
        else {
            return Objects.requireNonNull(mapper.apply(val));
        }

    }
	
	@SafeVarargs
	public final Lambda<T> exec(Consumer<T>...cs){
		Objects.requireNonNull(cs);
		for(Consumer<T> c : cs)
			c.accept(val);
		return this;
	}
	
	@SafeVarargs
	public final Lambda<T> execIf(Predicate<T> p, Consumer<T>...cs) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(cs);
		if(p.test(val))
			for(Consumer<T> c : cs)
				c.accept(val);
		return this;
	}
	
	@SafeVarargs
	public final Lambda<T> ifPresent(Consumer<T>...cs){
		Objects.requireNonNull(cs);
		if(val != null)
			for(Consumer<T> c : cs)
				c.accept(val);
		return this;
	}
	
	public Lambda<T> ifPresent(Runnable...rs) {
		Objects.requireNonNull(rs);
		for(Runnable r : rs)
			r.run();
		return this;
	}
	
	public Lambda<T> ifAbsent(Runnable...rs) {
		Objects.requireNonNull(rs);
		for(Runnable r : rs)
			r.run();
		return this;
	}
	
	@SafeVarargs
	public final Lambda<T> ifAbsent(Consumer<Lambda<T>>...cs) {
		Objects.requireNonNull(cs);
		for(Consumer<Lambda<T>> c : cs)
			c.accept(this);
		return this;
	}
	
	public <X extends Throwable> T getOrThrow(Supplier<X> ex) throws X {
		Objects.requireNonNull(ex);
		if(val == null)
			throw ex.get();
		return val;
	}
	
	public Lambda<T> set(Supplier<T> t) {
		Objects.requireNonNull(t);
		this.val = t.get();
		return this;
	}
	
	public Lambda<T> set(T t) {
		this.val = t;
		return this;
	}
	
	public Lambda<T> setIf(Predicate<T> p, Supplier<T> s) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(s);
		if(p.test(val))
			val = s.get();
		return this;
	}
	
	public Lambda<T> setIf(Predicate<T> p, Function<T, T> f) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(f);
		if(p.test(val))
			val = f.apply(val);
		return this;
	}
	
	public T getOrElse(T t) {
		return this.val == null ? t : this.val;
	}
	
	public T getOrElse(Supplier<T> t) {
		Objects.requireNonNull(t);
		return this.val == null ? t.get() : this.val;
	}
}
