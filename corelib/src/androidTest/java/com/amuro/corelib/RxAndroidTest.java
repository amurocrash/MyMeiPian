package com.amuro.corelib;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Amuro on 2017/12/26.
 */
@RunWith(AndroidJUnit4.class)
public class RxAndroidTest
{
	private void sysout(Object obj)
	{
//		System.out.println(obj);
		Log.v("rxtest", obj.toString());
	}

	/**
	 * 1.基础实例，注意Rx的代码逻辑是被订阅者（Observable）订阅（subscribe）订阅者（Subscriber），
	 * 有点反直觉，但本质逻辑还是反过来的。
	 * 2.Subscriber是Observer的子类。
	 */
	public void test1()
	{
		Observer<String> observer = new Observer<String>()
		{
			@Override
			public void onCompleted()
			{
				System.out.println("observer on Completed");
			}

			@Override
			public void onError(Throwable e)
			{
				System.out.println("observer on Error " + e.getMessage());
			}

			@Override
			public void onNext(String s)
			{
				System.out.println("observer on Next " + s);
			}
		};

		Subscriber<String> subscriber = new Subscriber<String>()
		{
			@Override
			public void onCompleted()
			{
				System.out.println("subscriber on Complete");
			}

			@Override
			public void onError(Throwable e)
			{
				System.out.println("subscriber on Error " + e.getMessage());
			}

			@Override
			public void onNext(String s)
			{
				System.out.println("subscriber on Next " + s);
			}
		};


		Observable<String> observable = Observable.create(
				new Observable.OnSubscribe<String>()
				{
					@Override
					public void call(Subscriber<? super String> subscriber)
					{
						subscriber.onNext("1");
						subscriber.onNext("2");
						subscriber.onNext("3");
						subscriber.onCompleted();
					}
				});
		observable.subscribe(subscriber);
	}

	/**********************************************************************************/

	/**
	 * 1.Action1，Action1，Action0对应一个Observer的onNext，onError和onComplete方法，
	 * 本质是源码中会把这三个方法汇聚到一个Subscriber里。
	 * 2.这样做的思想有点像js直接传函数的套路，同时使得开发者不需要处理onError和onComplete时，
	 * 可以直接通过传函数的方式来实现。
	 */
	public void test2()
	{
		Observable<String> observable = Observable.create(
				new Observable.OnSubscribe<String>()
				{
					@Override
					public void call(Subscriber<? super String> subscriber)
					{
						subscriber.onNext("1");
						subscriber.onNext("2");
						subscriber.onNext("3");
						subscriber.onCompleted();
					}
				});
		observable.subscribe(
				new Action1<String>()
				{
					@Override
					public void call(String s)
					{
						System.out.println("action1: " + s);
					}
				},

				new Action1<Throwable>()
				{
					@Override
					public void call(Throwable throwable)
					{
						System.out.println("action1 error: " + throwable.getMessage());
					}
				},
				new Action0()
				{

					@Override
					public void call()
					{
						System.out.println("action0");
					}
				});

	}

	/**********************************************************************************/

	/**
	 * from和just方法可以直接传递多个参数，然后Action1的call方法也会被多次回调。
	 */
	public void test3()
	{
		Observable.from(new String[]{"1", "2", "3"}).
				subscribe(new Action1<String>()
				{
					@Override
					public void call(String string)
					{
						System.out.println("from: " + string);
					}
				});

		Observable.just(1, 2, 3).
				subscribe(new Action1<Integer>()
				{
					@Override
					public void call(Integer integer)
					{
						System.out.println("just: " + integer);
					}
				});

	}

	/**********************************************************************************/

	private static class Person
	{
		Data data;

		@Override
		public String toString()
		{
			return "Person{" +
					"data=" + data +
					'}';
		}
	}

	private static class Data
	{
		String name;
		int age;

		@Override
		public String toString()
		{
			return "Data{" +
					"name='" + name + '\'' +
					", age=" + age +
					'}';
		}
	}

	/**
	 * 多线程切换，subscribeOn是call方法运行的线程，observerOn是Observer回调方法运行的线程,
	 * 可以把subscribeOn理解成observableOn。
	 */
	public void test4()
	{
		final Person person = new Person();
		Observable.create(new Observable.OnSubscribe<Data>()
		{
			@Override
			public void call(Subscriber<? super Data> subscriber)
			{
				System.out.println(
						"Observable call in " + Thread.currentThread().getName());
				Data data = new Data();
				data.name = "amuro";
				data.age = 18;
				subscriber.onNext(data);
				subscriber.onCompleted();
			}
		}).
				subscribeOn(Schedulers.io()).
				observeOn(Schedulers.computation()).
				subscribe(new Observer<Data>()
				{
					@Override
					public void onCompleted()
					{

					}

					@Override
					public void onError(Throwable e)
					{

					}

					@Override
					public void onNext(Data data)
					{
						person.data = data;
						System.out.println(
								"Observer onNext in " + Thread.currentThread().getName());
						System.out.println(person.toString());
					}
				});
	}

	/**********************************************************************************/

	private static class Student
	{
		private Course[] courses;

		public Student(String name)
		{
			this.name = name;
		}

		String name;

		public Course[] getCourses()
		{
			return courses;
		}
	}

	private static class Course
	{
		public Course(String name)
		{
			this.name = name;
		}

		String name;
	}

	/**
	 * 通过map方法进行Object的转换，一对一
	 */
	public void test5()
	{
		Student[] students =
				new Student[]{new Student("amuro"), new Student("char")};
		Observable.from(students).
				map(new Func1<Student, String>()
				{
					@Override
					public String call(Student student)
					{
						return student.name;
					}
				}).
				subscribe(new Action1<String>()
				{
					@Override
					public void call(String s)
					{
						System.out.println("Student name is " + s);
					}
				});
	}

	/**
	 * 一对多
	 */
	public void test6()
	{
		Student stu1 = new Student("amuro");
		stu1.courses = new Course[]{new Course("English"), new Course("Computer")};
		Student stu2 = new Student("char");
		stu2.courses = new Course[]{new Course("Math"), new Course("Music")};

		Student[] students =
				new Student[]{stu1, stu2};

		Observable.from(students).
				flatMap(new Func1<Student, Observable<Course>>()
				{
					@Override
					public Observable<Course> call(Student student)
					{
						return Observable.from(student.getCourses());
					}
				}).
				subscribe(new Action1<Course>()
				{
					@Override
					public void call(Course course)
					{
						System.out.println(course.name);
					}
				});
	}

	/**********************************************************************************/

	/**
	 * Rx不推荐使用lift，just for example
	 * 正式写法参考test8，7的写法是为了方便理解，其实就是个代理模式
	 */
	public void test7()
	{
		Integer[] arr = new Integer[]{1, 2, 3};

		Observable<Integer> oOrigin = Observable.from(arr);
		Observable<String> oNew = oOrigin.lift(new Observable.Operator<String, Integer>()
		{
			@Override
			public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber)
			{
				return new Subscriber<Integer>()
				{
					@Override
					public void onCompleted()
					{

					}

					@Override
					public void onError(Throwable e)
					{

					}

					@Override
					public void onNext(Integer integer)
					{
						subscriber.onNext("Integer is " + integer);
					}
				};
			}
		});

		oNew.subscribe(new Subscriber<String>()
		{
			@Override
			public void onCompleted()
			{

			}

			@Override
			public void onError(Throwable e)
			{

			}

			@Override
			public void onNext(String s)
			{
				sysout(s);
			}
		});
	}

	public void test8()
	{
		Observable.
				from(new Integer[]{1, 2, 3}).
				lift(new Observable.Operator<String, Integer>()
				{
					@Override
					public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber)
					{
						return new Subscriber<Integer>()
						{
							@Override
							public void onCompleted()
							{

							}

							@Override
							public void onError(Throwable e)
							{

							}

							@Override
							public void onNext(Integer integer)
							{
								subscriber.onNext("Hello world: " + integer);
							}
						};
					}
				}).
				subscribe(new Action1<String>()
				{
					@Override
					public void call(String s)
					{
						sysout(s);
					}
				});
	}

	/**********************************************************************************/

	/**
	 * 线程切换模型，map方法会运行在它上面observerOn设定的线程中，
	 * 而observer的回调（Action1）会在最后observerOn设定的线程中，这里就是安卓的主线程
	 */
	public void test9() throws Exception
	{
		Observable.
				just(1, 2, 3, 4).
				subscribeOn(Schedulers.io()).
				observeOn(Schedulers.newThread()).
				map(new Func1<Integer, String>()
				{

					@Override
					public String call(Integer integer)
					{
						sysout("map1： " + Thread.currentThread().getName());
						return "Integer to String: " + integer;
					}
				}).
				observeOn(AndroidSchedulers.mainThread()).
				map(new Func1<String, String>()
				{
					@Override
					public String call(String str)
					{
						sysout("map2: " + Thread.currentThread().getName());
						return "String to String: " + str;
					}
				}).
				subscribe(new Action1<String>()
				{
					@Override
					public void call(String s)
					{
						sysout("observer: " + Thread.currentThread().getName());
					}
				});
	}

	/**********************************************************************************/

	private static class User
	{
		String userId;
		String userName;
		String nickName;
		int age;

		@Override
		public String toString()
		{
			return "User{" +
					"userId='" + userId + '\'' +
					", userName='" + userName + '\'' +
					", nickName='" + nickName + '\'' +
					", age=" + age +
					'}';
		}
	}

	/**
	 * 模拟网络请求一个entity的实际场景
	 */
	public void test10()
	{
		final String username = "admin";
		final String password = "admin";

		Observable.just("").
				observeOn(Schedulers.io()).
				map(new Func1<String, User>()
				{
					@Override
					public User call(String s)
					{
						if ("admin".equals(username) && "admin".equals(password))
						{
							User user = new User();
							user.userId = "1";
							user.nickName = "nick";
							user.userName = "admin";
							user.age = 18;

							return user;
						}

						return null;
					}
				}).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(new Action1<User>()
				{
					@Override
					public void call(User user)
					{
						if (user != null)
						{
							sysout(user);
						}
					}
				});
	}

	/**
	 * 模拟嵌套请求，这个最能体现RxJava的优势，否则将是callback嵌套地狱
	 */
	public void test11()
	{
		final String username = "admin";
		final String password = "admin";
		Observable.
				just("").
				observeOn(Schedulers.io()).
				flatMap(new Func1<String, Observable<String>>()
				{
					@Override
					public Observable<String> call(String s)
					{
						sysout("token request in " + Thread.currentThread().getName());
						String token = null;

						if ("admin".equals(username) && "admin".equals(password))
						{
							token = "1234";
						}

						return Observable.just(token);
					}
				}).
				observeOn(Schedulers.io()).
				map(new Func1<String, String>()
				{
					@Override
					public String call(String token)
					{
						sysout("token verify in " + Thread.currentThread().getName());
						if ("1234".equals(token))
						{
							return "0";
						}

						return "1";
					}
				}).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(new Action1<String>()
				{
					@Override
					public void call(String s)
					{
						sysout("final call back in " + Thread.currentThread().getName());
						if ("0".equals(s))
						{
							sysout("login success!");
						}
						else
						{
							sysout("login failed!");
						}
					}
				});
	}

	/**
	 * 模拟获取数据集合
	 */
	public void test12()
	{
		Observable.
				just("").
				observeOn(Schedulers.io()).
				map(new Func1<String, List<User>>()
				{
					@Override
					public List<User> call(String s)
					{
						List<User> userList = new ArrayList<>();
						for(int i = 0; i < 3; i++)
						{
							userList.add(new User());
						}

						return userList;
					}
				}).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(new Action1<List<User>>()
				{
					@Override
					public void call(List<User> users)
					{
						sysout(users.size());
					}
				});
	}

	/**
	 * 完整的包含错误处理的entity请求例子
	 */
	public void test13()
	{
		final String username = "admin";
		final String password = "adminx";

		Observable.create(new Observable.OnSubscribe<List<User>>()
		{
			@Override
			public void call(Subscriber<? super List<User>> subscriber)
			{
				if(!subscriber.isUnsubscribed())
				{

					if ("admin".equals(username) && "admin".equals(password))
					{

						List<User> userList = new ArrayList<>();
						for (int i = 0; i < 3; i++)
						{
							userList.add(new User());
						}
						subscriber.onNext(userList);
						subscriber.onCompleted();
					}
					else
					{
						subscriber.onError(new Exception("error"));
					}
				}
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
		subscribe(new Subscriber<List<User>>()
		{
			@Override
			public void onCompleted()
			{
				sysout("completed");
			}

			@Override
			public void onError(Throwable e)
			{
				sysout(e.getMessage());
			}

			@Override
			public void onNext(List<User> users)
			{
				sysout("Get " + users.size() + " users");
			}
		});
	}

	/**
	 * 完整的包含错误处理的嵌套请求例子
	 */
	@Test
	public void test14()
	{
		final String username = "admin";
		final String password = "admin";

		Observable.create(new Observable.OnSubscribe<String>()
		{
			@Override
			public void call(Subscriber<? super String> subscriber)
			{
				sysout("1 " + Thread.currentThread().getName());
				if("admin".equals(username) && "admin".equals(password))
				{
					String token = "1234";
					subscriber.onNext(token);
					subscriber.onCompleted();
				}
				else
				{
					subscriber.onError(new Exception("username or password error"));
				}

			}
		}).
		subscribeOn(Schedulers.io()).
		lift(new Observable.Operator<User, String>()
		{
			@Override
			public Subscriber<? super String> call(final Subscriber<? super User> subscriber)
			{
				return new Subscriber<String>()
				{
					@Override
					public void onCompleted()
					{

					}

					@Override
					public void onError(Throwable e)
					{
						subscriber.onError(e);
					}

					@Override
					public void onNext(String token)
					{
						sysout("2 " + Thread.currentThread().getName());
						if("1234".equals(token))
						{
							User user = new User();
							user.userName = "hehe";
							subscriber.onNext(user);
							subscriber.onCompleted();
						}
						else
						{
							subscriber.onError(new Exception("token error"));
						}
					}
				};
			}
		}).
		observeOn(AndroidSchedulers.mainThread()).
		subscribe(new Subscriber<User>()
		{
			@Override
			public void onCompleted()
			{
				sysout("on Completed");
			}

			@Override
			public void onError(Throwable e)
			{
				sysout("on error: " + e.getMessage());
			}

			@Override
			public void onNext(User user)
			{
				sysout("3 " + Thread.currentThread().getName());
				sysout("on Success: " + user.userName);
			}
		});



	}
}


































