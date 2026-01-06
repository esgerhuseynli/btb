import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/constants/app_constants.dart';
import '../../core/widgets/app_button.dart';
import '../../core/widgets/app_text_field.dart';
import '../../core/widgets/app_app_bar.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_event.dart';
import '../bloc/auth_state.dart';

class SignUpByCifScreen extends StatefulWidget {
  const SignUpByCifScreen({super.key});

  @override
  State<SignUpByCifScreen> createState() => _SignUpByCifScreenState();
}

class _SignUpByCifScreenState extends State<SignUpByCifScreen> {
  final _formKey = GlobalKey<FormState>();
  final _cifController = TextEditingController();
  final _dateOfBirthController = TextEditingController();
  DateTime? _selectedDate;

  @override
  void dispose() {
    _cifController.dispose();
    _dateOfBirthController.dispose();
    super.dispose();
  }

  Future<void> _selectDate(BuildContext context) async {
    final DateTime now = DateTime.now();
    final DateTime firstDate = DateTime(now.year - 100);
    final DateTime lastDate = DateTime(now.year - 18);

    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _selectedDate ?? lastDate,
      firstDate: firstDate,
      lastDate: lastDate,
      // Use the locale from MaterialApp, or default to system locale
      // locale will be picked up from MaterialApp's supportedLocales
    );

    if (picked != null && picked != _selectedDate) {
      setState(() {
        _selectedDate = picked;
        // Android format: %02d-%02d-%d = dd-MM-yyyy (e.g., "15-03-1990")
        _dateOfBirthController.text = DateFormat('dd-MM-yyyy').format(picked);
      });
      // Auto-submit when date is selected (like Android)
      if (_formKey.currentState!.validate()) {
        _handleSubmit();
      }
    }
  }

  bool _isFormValid() {
    if (_cifController.text.length != 6) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('CIF 6 simvol olmalıdır')),
      );
      return false;
    }

    if (_selectedDate == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Doğum tarixini seçin')),
      );
      return false;
    }

    return true;
  }

  Future<void> _handleSubmit() async {
    if (!_formKey.currentState!.validate() || !_isFormValid()) {
      return;
    }

    final cif = _cifController.text.trim();
    // Android format: dd-MM-yyyy (e.g., "15-03-1990")
    final birthdate = DateFormat('dd-MM-yyyy').format(_selectedDate!);
    
    // Store CIF and birthdate for later use (like Android's AppData)
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('signUpCif', cif);
    await prefs.setString('signUpDateOfBirth', birthdate);
    
    // Create event that will build the request in AuthBloc
    context.read<AuthBloc>().add(
          SendCardNumberForCifEvent(
            cif: cif,
            birthdate: birthdate,
          ),
        );
  }

  Future<void> _openPrivacyPolicy() async {
    // Get current language
    final prefs = await SharedPreferences.getInstance();
    final langIndex = prefs.getInt(AppConstants.appLanguage) ?? 0;
    final languages = ['az', 'en', 'ru'];
    final lang = languages[langIndex];
    
    final url = Uri.parse('https://www.btb.az/$lang/license-agreement');
    if (await canLaunchUrl(url)) {
      await launchUrl(url, mode: LaunchMode.externalApplication);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.mainBackground,
      appBar: AppAppBar(
        title: 'Qeydiyyat',
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => context.pop(),
        ),
      ),
      body: SafeArea(
        child: BlocListener<AuthBloc, AuthState>(
          listener: (context, state) async {
            if (state is AuthError) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(state.message),
                  backgroundColor: AppTheme.red,
                ),
              );
            } else if (state is CodeSent) {
              // Navigate to verification screen
              context.push(
                '/verification',
                extra: {
                  'requestType': AppConstants.signUpTypeCif,
                  'phone': state.phone,
                  'email': state.email,
                  // CardSendRequest will be rebuilt in verification screen if needed for resend
                },
              );
            }
          },
          child: BlocBuilder<AuthBloc, AuthState>(
            builder: (context, state) {
              final isLoading = state is AuthLoading;

              return SingleChildScrollView(
                padding: const EdgeInsets.all(24.0),
                child: Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      const SizedBox(height: 32),
                      Text(
                        'CIF ilə qeydiyyat',
                        style: Theme.of(context).textTheme.titleLarge,
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 50),
                      AppTextField(
                        controller: _cifController,
                        label: 'CIF daxil edin',
                        hint: 'CIF',
                        keyboardType: TextInputType.text,
                        maxLength: 6,
                        inputFormatters: [
                          FilteringTextInputFormatter.allow(
                            RegExp(r'[0-9A-Za-z]'),
                          ),
                        ],
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'CIF daxil edin';
                          }
                          if (value.length != 6) {
                            return 'CIF 6 simvol olmalıdır';
                          }
                          return null;
                        },
                        onSubmitted: (_) {
                          // Focus on date field
                          FocusScope.of(context).nextFocus();
                        },
                      ),
                      const SizedBox(height: 16),
                      Stack(
                        children: [
                          AppTextField(
                            controller: _dateOfBirthController,
                            label: 'Doğum tarixi',
                            hint: 'dd-MM-yyyy',
                            keyboardType: TextInputType.datetime,
                            suffixIcon: const Icon(Icons.calendar_today),
                            enabled: false, // Disable text input
                            validator: (value) {
                              if (value == null || value.isEmpty) {
                                return 'Doğum tarixini seçin';
                              }
                              return null;
                            },
                          ),
                          Positioned.fill(
                            child: Material(
                              color: Colors.transparent,
                              child: InkWell(
                                onTap: () => _selectDate(context),
                                borderRadius: BorderRadius.circular(4),
                              ),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 25),
                      // Privacy policy text
                      RichText(
                        textAlign: TextAlign.center,
                        text: TextSpan(
                          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                fontSize: 11,
                              ),
                          children: [
                            const TextSpan(
                              text: 'Davam etməklə ',
                            ),
                            WidgetSpan(
                              child: GestureDetector(
                                onTap: _openPrivacyPolicy,
                                child: Text(
                                  'Məxfilik Siyasəti',
                                  style: TextStyle(
                                    color: AppTheme.mainColor,
                                    fontSize: 11,
                                    decoration: TextDecoration.underline,
                                  ),
                                ),
                              ),
                            ),
                            const TextSpan(
                              text: 'ni qəbul etmiş olursunuz',
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 32),
                      AppButton(
                        text: 'Davam et',
                        onPressed: isLoading ? null : _handleSubmit,
                        isLoading: isLoading,
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ),
    );
  }
}

