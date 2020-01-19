Name:          uom-se
Version:       1.0.4
Release:       3%{?dist}
Summary:       Unit Standard (JSR 363) implementation for Java SE 8 and above
License:       BSD
URL:           https://github.com/unitsofmeasurement/%{name}
Source0:       https://github.com/unitsofmeasurement/%{name}/archive/%{version}/%{name}-%{version}.tar.gz

BuildRequires: junit
BuildRequires: maven-local
BuildRequires: maven-jar-plugin
BuildRequires: maven-install-plugin
BuildRequires: maven-dependency-plugin
BuildRequires: mvn(org.hamcrest:hamcrest-all)
BuildRequires: mvn(javax.measure:unit-api)
BuildRequires: mvn(tec.uom:uom-parent:pom:)
BuildRequires: mvn(tec.uom.lib:uom-lib:pom:)

BuildArch:     noarch

%description
JSR 363 Implementation got Java SE 8 and above.

JDK Integration of Unit-API / JSR 363.  This implementation aims at
Java SE 8 and above, allowing the use of new features like Lambdas
together with Units of Measurement API.

%package javadoc
BuildArch: noarch
Summary: Javadoc for the Units Standard (JSR 363) Java SE 8 Implementation

%description javadoc
This package contains documentation for the Units Standard (JSR 363)
Java SE 8 Implementation.

%prep
%setup -q -n %{name}-%{version}

# Unavailable plugins
%pom_remove_plugin :maven-bundle-plugin
%pom_remove_plugin :license-maven-plugin
%pom_remove_plugin :formatter-maven-plugin
%pom_xpath_set "pom:packaging" jar

%mvn_file : %{name}

%build
%mvn_build

%install
%mvn_install

%files -f .mfiles
%doc README.md
%license LICENSE.txt

%files javadoc -f .mfiles-javadoc

%changelog
* Fri Apr 07 2017 Nathan Scott <nathans@redhat.com> - 1.0.4-3
- Spec file changes and pom updates for building on RHEL7.

* Wed Mar 22 2017 Nathan Scott <nathans@redhat.com> - 1.0.4-2
- Incorprate feedback from gil cattaneo on all uom packages.

* Mon Mar 06 2017 Nathan Scott <nathans@redhat.com> - 1.0.4-1
- Update to latest upstream sources.

* Tue Feb 28 2017 Nathan Scott <nathans@redhat.com> - 1.0.2-2
- Resolve lintian errors - source, license, documentation.

* Fri Feb 24 2017 Nathan Scott <nathans@redhat.com> - 1.0.2-1
- Add unitsofmeasurement prefix to package name.
- Update to latest upstream sources.

* Fri Nov 25 2016 Nathan Scott <nathans@redhat.com> - 1.0.1-1
- Initial version.
